package io.taig.bsts.report

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.Term.Aux
import io.taig.bsts.ops.NestedEvaluation
import io.taig.bsts.{ Error, Term }
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
}