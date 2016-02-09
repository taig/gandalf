package io.taig.bsts.report

import io.taig.bsts._
import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.report.syntax.report._
import shapeless._
import shapeless.ops.function.FnToProduct
import shapeless.ops.hlist.LeftFolder

trait Report[-T] {
    type Out

    def report( context: T ): Out
}

object Report {
    type Aux[T, Out0] = Report[T] { type Out = Out0 }

    def apply[I <: String, A <: HList]( f: A ⇒ String ): Report.Aux[Error[I, A], String] = new Report[Error[I, A]] {
        override type Out = String

        override def report( error: Error[I, A] ): String = f( error.arguments )
    }

    def apply[N <: String, A <: HList](
        rule: Rule[N, _, A]
    )(
        message: String
    ): Report.Aux[Error[N, A], String] = Report( _ ⇒ message )

    def apply[N <: String, A <: HList](
        transformation: Transformation[N, _, _, A]
    )(
        message: String
    ): Report.Aux[Error[N, A], String] = Report( _ ⇒ message )

    def apply[N <: String, A <: HList, F, L, R]( rule: F )(
        implicit
        ftp: FnToProduct.Aux[F, L ⇒ R],
        ev1: R <:< Rule[N, _, A]
    ): Builder[N, A] = new Builder

    class Builder[I <: String, A <: HList] {
        def as( f: A ⇒ String ): Report.Aux[Error[I, A], String] = Report( f )
    }

    implicit def `Report[Failure[Rule|Transformation]]`[I <: String, T, A <: HList](
        implicit
        r: Report[Error[I, A]]
    ): Report.Aux[Failure[Error[I, A], T], r.Out] = new Report[Failure[Error[I, A], T]] {
        override type Out = r.Out

        override def report( failure: Failure[Error[I, A], T] ): Out = failure.value.report
    }

    // TODO dynamic output type???
    implicit def `Report[Failure[Policy]]`[C <: HList, T](
        implicit
        lf: collect.F[C]
    ): Report.Aux[Failure[Computed[C], T], lf.Out] = new Report[Failure[Computed[C], T]] {
        override type Out = lf.Out

        override def report( failure: Failure[Computed[C], T] ): Out = {
            failure.value.tree.foldLeft( List.empty[String] )( collect )
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[String], this.type, List[String]]

        implicit def result[I <: String, T, A <: HList](
            implicit
            r: Report.Aux[Error[I, A], String]
        ) = at[List[String], Result[Error[I, A], T]] {
            case ( errors, Failure( error ) ) ⇒ errors :+ error.report
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