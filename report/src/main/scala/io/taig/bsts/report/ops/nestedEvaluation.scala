package io.taig.bsts.report.ops

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.Term
import io.taig.bsts.ops.Computed
import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.report.ReportableError
import shapeless.{ ::, HList, HNil }

trait nestedEvaluation {
    implicit def termReportableError[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, Validated[ReportableError[N, A], O]] :: HNil] {
            override type Out0 = Validated[ReportableError[N, A], O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, Validated[ReportableError[N, A], O]] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Valid( output ) ⇒ ( Some( output ), Computed( v :: HNil ) )
                    case i @ Invalid( _ )    ⇒ ( None, Computed( i :: HNil ) )
                }
            }
        }
    }
}

object nestedEvaluation extends nestedEvaluation