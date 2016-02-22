package io.taig.bsts.report

import cats.data.Validated.Valid
import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts._
import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.report.syntax.report._
import shapeless._
import shapeless.ops.hlist.LeftFolder

trait Report[-I] {
    type Out

    def report( input: I ): Out
}

object Report extends Report0 {
    def instance[N <: String, A <: HList]( f: A ⇒ String ): Report.Aux[Error[N, A], String] = new Report[Error[N, A]] {
        override type Out = String

        override def report( error: Error[N, A] ): String = f( error.arguments )
    }

    /**
     * Construct a Report for a Rule
     *
     * {{{
     * Report( rule.required )( _ => "required" )
     * Report( rule.min )( args => s"min ${args("expected")}" )
     * }}}
     */
    def apply[N <: String, A <: HList]( term: Term[N, _, _, A] )( message: A ⇒ String ): Report.Aux[Error[N, A], String] = {
        instance( message )
    }

    def apply[N <: String, A <: HList]( term: ( _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ String
    ): Report.Aux[Error[N, A], String] = instance( message )

    def apply[N <: String, A <: HList]( term: ( _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ String
    ): Report.Aux[Error[N, A], String] = instance( message )

    def apply[N <: String, A <: HList]( term: ( _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ String
    ): Report.Aux[Error[N, A], String] = instance( message )

    def apply[N <: String, A <: HList]( term: ( _, _, _, _ ) ⇒ Term[N, _, _, A] )(
        message: A ⇒ String
    ): Report.Aux[Error[N, A], String] = instance( message )

    implicit def reportReportableError[N <: String, A <: HList]: Aux[ReportableError[N, A], String] = {
        new Report[ReportableError[N, A]] {
            override type Out = String

            override def report( error: ReportableError[N, A] ): Out = error.delegateReport
        }
    }

    implicit def reportValidatedReportableError[N <: String, O, A <: HList](
        implicit
        r: Aux[ReportableError[N, A], String]
    ): Aux[Validated[ReportableError[N, A], O], Validated[String, O]] = {
        new Report[Validated[ReportableError[N, A], O]] {
            override type Out = Validated[String, O]

            override def report( validated: Validated[ReportableError[N, A], O] ) = validated.leftMap( _.report )
        }
    }

    implicit def reportTermValidated[N <: String, I, O, A <: HList](
        implicit
        r: Aux[Error[N, A], String]
    ): Aux[Validated[Error[N, A], O], Validated[String, O]] = new Report[Validated[Error[N, A], O]] {
        override type Out = Validated[String, O]

        override def report( validated: Validated[Error[N, A], O] ) = validated.leftMap( _.report )
    }

    implicit def reportPolicyValidated[C <: HList, O](
        implicit
        lf: collect.F[C]
    ): Aux[Validated[C, O], Validated[NonEmptyList[String], O]] = {
        new Report[Validated[C, O]] {
            override type Out = Validated[NonEmptyList[String], O]

            override def report( validated: Validated[C, O] ): Out = validated.leftMap { computation ⇒
                val list = computation.foldLeft( List.empty[String] )( collect )
                NonEmptyList( list.head, list.tail )
            }
        }
    }

    object collect extends Poly2 {
        type F[H <: HList] = LeftFolder.Aux[H, List[String], this.type, List[String]]

        implicit def error[N <: String, O, A <: HList](
            implicit
            r: Report.Aux[Validated[Error[N, A], O], Validated[String, O]]
        ): Case.Aux[List[String], Validated[Error[N, A], O], List[String]] = at { ( errors, validated ) ⇒
            errors ++ r.report( validated ).fold( List( _ ), _ ⇒ Nil )
        }

        implicit def reportableError[N <: String, O, A <: HList](
            implicit
            r: Report.Aux[Validated[ReportableError[N, A], O], Validated[String, O]]
        ): Case.Aux[List[String], Validated[ReportableError[N, A], O], List[String]] = at { ( errors, validated ) ⇒
            errors ++ r.report( validated ).fold( List( _ ), _ ⇒ Nil )
        }

        implicit def valid[O]: Case.Aux[List[String], Valid[O], List[String]] = at { ( errors, _ ) ⇒ errors }

        implicit def operator[O <: Operator]: Case.Aux[List[String], O, List[String]] = at { ( errors, _ ) ⇒ errors }

        implicit def recursion[L <: HList](
            implicit
            lf: F[L]
        ): Case.Aux[List[String], L, List[String]] = at {
            case ( errors, tree ) ⇒ tree.foldLeft( errors )( this )
        }

        implicit def coproduct[U <: HList, C <: HList](
            implicit
            lf: F[C]
        ): Case.Aux[List[String], C :+: U :+: CNil, List[String]] = at {
            case ( errors, Inl( tree ) ) ⇒ tree.foldLeft( errors )( this )
            case ( errors, _ )           ⇒ errors
        }
    }
}

trait Report0 {
    type Aux[I, O] = Report[I] { type Out = O }

    implicit def reportToNonEmptyListReport[A, B, O](
        implicit
        r: Aux[Validated[A, O], Validated[B, O]]
    ) = new Report[Validated[A, O]] {
        override type Out = Validated[NonEmptyList[B], O]

        override def report( input: Validated[A, O] ) = r.report( input ).leftMap( NonEmptyList( _ ) )
    }
}