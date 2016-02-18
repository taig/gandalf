package io.taig.bsts.report

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.{ Term, Error }
import io.taig.bsts.ops.hlist.NestedEvaluation
import shapeless.{ HNil, ::, HList }

case class ReportableError[N <: String, A <: HList](
        error: Error[N, A],
        r:     Report.Aux[Error[N, A], String]
) {
    def report = r.report( error )

    override def toString = error.toString
}

object ReportableError {
    implicit def nestedEvaluationReportableError[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, Validated[ReportableError[N, A], O]] :: HNil] {
            type R = Validated[ReportableError[N, A], O]

            override type Out0 = R :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, R] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Valid( output ) ⇒ ( Some( output ), v :: HNil )
                    case i @ Invalid( _ )    ⇒ ( None, i :: HNil )
                }
            }
        }
    }
}