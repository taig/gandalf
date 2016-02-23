package io.taig.bsts.report

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.ops.NestedEvaluation
import io.taig.bsts.syntax.raw._
import io.taig.bsts.{ Error, Raw, Term }
import shapeless.{ ::, HList, HNil }

case class ReportableError[N <: String, A <: HList, O](
        error: Error[N, A],
        r:     Report[Error[N, A], O]
) {
    def delegateReport = r.report( error )

    override def toString = error.toString
}

object ReportableError {
    implicit def nestedEvaluationReportableError[N <: String, I, O, A <: HList, P] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, ReportableError[N, A, P]] :: HNil] {
            override type Out0 = Validated[ReportableError[N, A, P], O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, ReportableError[N, A, P]] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Valid( output ) ⇒ ( Some( output ), v :: HNil )
                    case i @ Invalid( _ )    ⇒ ( None, i :: HNil )
                }
            }
        }
    }

    implicit def rawReportableError[N <: String, A <: HList, O](
        implicit
        r: Raw[Error[N, A]]
    ): Raw[ReportableError[N, A, O]] = new Raw[ReportableError[N, A, O]] {
        override def raw( value: ReportableError[N, A, O] ) = value.error.raw
    }

    implicit def reportReportableError[N <: String, A <: HList, O]: Report[ReportableError[N, A, O], O] = {
        new Report[ReportableError[N, A, O], O] {
            override def report( error: ReportableError[N, A, O] ) = error.delegateReport
        }
    }
}