package io.taig.bsts

import shapeless._
import shapeless.ops.function.FnToProduct
import io.taig.bsts.syntax.all._
import shapeless.ops.hlist.LeftFolder

trait Report[T] {
    type Out

    def report( context: T ): Out
}

object Report {
    type Aux[T, Out0] = Report[T] { type Out = Out0 }

    def apply[I <: String, A <: HList]( f: A ⇒ String ): Report.Aux[Error[I, A], String] = new Report[Error[I, A]] {
        override type Out = String

        override def report( error: Error[I, A] ): String = f( error.arguments )
    }

    def apply[I <: String, A <: HList]( rule: Rule[I, _, A] )( message: String ): Report.Aux[Error[I, A], String] = {
        Report( _ ⇒ message )
    }

    def apply[I <: String, A <: HList, F, L, R]( rule: F )(
        implicit
        ftp: FnToProduct.Aux[F, L ⇒ R],
        ev1: R <:< Rule[I, _, A]
    ): Builder[I, A] = new Builder

    class Builder[I <: String, A <: HList] {
        def as( f: A ⇒ String ): Report.Aux[Error[I, A], String] = Report( f )
    }

    implicit def `Report[Failure[Rule]]`[I <: String, T, A <: HList](
        implicit
        r: Report.Aux[Error[I, A], String]
    ): Report.Aux[Failure[Error[I, A], T], String] = {
        new Report[Failure[Error[I, A], T]] {
            override type Out = String

            override def report( failure: Failure[Error[I, A], T] ): Out = failure.value.report
        }
    }

    implicit def `Report[Failure[Policy]]`[C <: HList, T](
        implicit
        lf: collect.F[C]
    ): Report.Aux[Failure[Computed[C], T], lf.Out] = {
        new Report[Failure[Computed[C], T]] {
            override type Out = lf.Out

            override def report( failure: Failure[Computed[C], T] ): Out = {
                failure.value.tree.foldLeft( List.empty[String] )( collect )
            }
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[String], this.type, List[String]]

        implicit def validation[I <: String, T, A <: HList](
            implicit
            r: Report.Aux[Error[I, A], String]
        ) = at[List[String], Validation[Error[I, A], T]] {
            case ( errors, Failure( error ) ) ⇒ errors :+ error.report
            case ( errors, _ )                ⇒ errors
        }

        implicit def operator[O <: Operator.Binary] = at[List[String], O]( ( errors, _ ) ⇒ errors )

        implicit def computed[L <: HList](
            implicit
            lf: F[L]
        ) = at[List[String], Computed[L]] {
            case ( errors, Computed( tree ) ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def either[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ) = at[List[String], Either[Unevaluated[U], Computed[C]]] {
            case ( errors, Right( Computed( tree ) ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, Left( Unevaluated( _ ) ) )  ⇒ errors
        }
    }
}