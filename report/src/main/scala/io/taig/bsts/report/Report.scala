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
    type E

    type Out

    def report( context: T ): Out
}

object Report extends Report0 {
    def instance[N <: String, A <: HList, R]( f: A ⇒ R ): Report.Aux[Error[N, A], R, R] = new Report[Error[N, A]] {
        override type E = Out

        override type Out = R

        override def report( error: Error[N, A] ): R = f( error.arguments )
    }

    /**
     * Construct a Report for a Rule
     *
     * {{{
     * Report( rule.required )( _ => "required" )
     * Report( rule.min )( args => s"min ${args("expected")}" )
     * }}}
     */
    def apply[N <: String, A <: HList, R]( term: Term[N, _, _, A] )( message: A ⇒ R ): Report.Aux[Error[N, A], R, R] = {
        instance( message )
    }

    def apply[N <: String, A <: HList, R]( term: ( _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R, R] = instance( message )

    def apply[N <: String, A <: HList, R]( term: ( _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R, R] = instance( message )

    def apply[N <: String, A <: HList, R]( term: ( _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R, R] = instance( message )

    def apply[N <: String, A <: HList, R]( term: ( _, _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ R
    ): Report.Aux[Error[N, A], R, R] = instance( message )

    implicit def `Report[Term]`[N <: String, I, O, A <: HList, R](
        implicit
        r: Report.Aux[Error[N, A], R, R]
    ): Report.Aux[Validated[Error[N, A], O], R, Validated[R, O]] = new Report[Validated[Error[N, A], O]] {
        override type E = R

        override type Out = Validated[E, O]

        override def report( validated: Validated[Error[N, A], O] ): Validated[R, O] = validated.leftMap( _.report[E] )
    }

    implicit def `Report[Policy]`[C <: HList, O, R](
        implicit
        lf: collect.F[C, R]
    ): Report.Aux[Validated[Computed[C], O], R, Validated[NonEmptyList[R], O]] = {
        new Report[Validated[Computed[C], O]] {
            override type E = R

            override type Out = Validated[NonEmptyList[E], O]

            override def report( validated: Validated[Computed[C], O] ): Out = validated.leftMap { computation ⇒
                val list = computation.tree.foldLeft( List.empty[R] )( collect )
                NonEmptyList( list.head, list.tail )
            }
        }
    }

    object collect extends collect0 {
        type F[H <: HList, R] = LeftFolder.Aux[H, List[R], this.type, List[R]]

        implicit def validated[N <: String, O, A <: HList, R](
            implicit
            r: Report.Aux[Validated[Error[N, A], O], R, Validated[R, O]]
        ): Case.Aux[List[R], Validated[Error[N, A], O], List[R]] = at { ( errors, validated ) ⇒
            errors ++ r.report( validated ).fold( List( _ ), _ ⇒ Nil )
        }

        implicit def computed[L <: HList, R](
            implicit
            lf: F[L, R]
        ): Case.Aux[List[R], Computed[L], List[R]] = at {
            case ( errors, Computed( tree ) ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def coproduct[U <: HList, C <: HList, R](
            implicit
            lf: F[C, R]
        ): Case.Aux[List[R], Computed[C] :+: Unevaluated[U] :+: CNil, List[R]] = at {
            case ( errors, Inl( Computed( tree ) ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )                       ⇒ errors
        }
    }

    trait collect0 extends Poly2 {
        implicit def identity[T, R]: Case.Aux[List[R], T, List[R]] = at { ( errors, _ ) ⇒ errors }
    }
}

trait Report0 {
    type Aux0[T, E0] = Report[T] { type E = E0 }

    type Aux[T, E0, Out0] = Report[T] { type E = E0; type Out = Out0 }

    implicit def `Report.Aux[Term, NonEmptyList[String]]`[N <: String, O, A <: HList, R](
        implicit
        r: Report.Aux[Validated[Error[N, A], O], R, Validated[R, O]]
    ): Report.Aux[Validated[Error[N, A], O], R, Validated[NonEmptyList[R], O]] = {
        new Report[Validated[Error[N, A], O]] {
            override type E = R

            override type Out = Validated[NonEmptyList[R], O]

            override def report( validated: Validated[Error[N, A], O] ) = {
                r.report( validated ).leftMap( NonEmptyList( _ ) )
            }
        }
    }
}