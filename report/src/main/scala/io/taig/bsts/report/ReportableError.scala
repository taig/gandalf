package io.taig.bsts.report

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.Term.Aux
import io.taig.bsts.{ Error, Term }
import io.taig.bsts.ops.hlist.NestedEvaluation
import shapeless.{ ::, HList, HNil }

case class ReportableError[N <: String, A <: HList](
        error: Error[N, A],
        r:     Report.Aux[Error[N, A], String]
) {
    def delegateReport = r.report( error )

    override def toString = error.toString
}

object ReportableError {
    implicit def nestedEvaluationReportableError[N <: String, I, O, A <: HList] = {
        type E = ReportableError[N, A]

        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, E] :: HNil] {
            override type Out0 = Validated[E, O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, E] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Valid( output ) ⇒ ( Some( output ), v :: HNil )
                    case i @ Invalid( _ )    ⇒ ( None, i :: HNil )
                }
            }
        }
    }
}