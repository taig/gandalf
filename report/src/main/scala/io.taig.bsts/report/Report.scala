package io.taig.bsts.report

import io.taig.bsts.data.{ NonEmptyList, Validated }
import Validated.Invalid
import io.taig.bsts._
import io.taig.bsts.ops.{ Unevaluated, Computed }
import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.report.syntax.report._
import shapeless._
import shapeless.ops.hlist.LeftFolder

trait Report[-T] {
    type Out

    def report( context: T ): Out
}

object Report extends Report0 {
    def instance[N <: String, A <: HList, O]( f: A ⇒ O ): Report.Aux[Error[N, A], O] = new Report[Error[N, A]] {
        override type Out = O

        override def report( error: Error[N, A] ): O = f( error.arguments )
    }

    /**
     * Construct a Report for a Rule
     *
     * {{{
     * Report( rule.required )( _ => "required" )
     * Report( rule.min )( args => s"min ${args("expected")}" )
     * }}}
     */
    def apply[N <: String, A <: HList, R]( term: Term[N, _, _, A] )( message: A ⇒ R ): Report.Aux[Error[N, A], R] = {
        instance( message )
    }

    def apply[N <: String, A <: HList, R]( term: ( _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R] = instance( message )

    def apply[N <: String, A <: HList, R]( term: ( _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R] = instance( message )

    def apply[N <: String, A <: HList, R]( term: ( _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R] = instance( message )

    def apply[N <: String, A <: HList, R]( term: ( _, _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R] = instance( message )

    implicit def `Report[Term]`[N <: String, I, O, A <: HList, R](
        implicit
        r: Report.Aux[Error[N, A], R]
    ): Report.Aux[Validated[Error[N, A], O], Validated[R, O]] = new Report[Validated[Error[N, A], O]] {
        override type Out = Validated[R, O]

        override def report( validated: Validated[Error[N, A], O] ): Validated[R, O] = {
            validated.leftMap( _.report )
        }
    }

    implicit def `Report[Policy]`[C <: HList, T](
        implicit
        lf: collect.F[C]
    ): Report.Aux[Validated[Computed[C], T], Validated[NonEmptyList[String], T]] = {
        new Report[Validated[Computed[C], T]] {
            override type Out = Validated[NonEmptyList[String], T]

            override def report( validated: Validated[Computed[C], T] ): Out = validated.leftMap { computation ⇒
                val list = computation.tree.foldLeft( List.empty[String] )( collect )
                NonEmptyList( list.head, list.tail )
            }
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[String], this.type, List[String]]

        implicit def result[I <: String, T, A <: HList](
            implicit
            r: Report.Aux[Error[I, A], String]
        ) = at[List[String], Validated[Error[I, A], T]] {
            case ( errors, Invalid( error ) ) ⇒ errors :+ error.report[String]
            case ( errors, _ )                ⇒ errors
        }

        implicit def operator[O <: Operator.Binary] = at[List[String], O]( ( errors, _ ) ⇒ errors )

        implicit def computed[L <: HList](
            implicit
            lf: F[L]
        ) = at[List[String], Computed[L]] { case ( errors, Computed( tree ) ) ⇒ tree.foldLeft( errors )( this ) }

        implicit def coproduct[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ) = at[List[String], Computed[C] :+: Unevaluated[U] :+: CNil] {
            case ( errors, Inl( Computed( tree ) ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )                       ⇒ errors
        }
    }
}

trait Report0 {
    type Aux[T, Out0] = Report[T] { type Out = Out0 }

    implicit def `Report.Aux[Term, List[String]]`[I <: String, T, A <: HList](
        implicit
        r: Report.Aux[Validated[Error[I, A], T], String]
    ): Report.Aux[Validated[Error[I, A], T], List[String]] = new Report[Validated[Error[I, A], T]] {
        override type Out = List[String]

        override def report( validated: Validated[Error[I, A], T] ) = List( r.report( validated ) )
    }
}