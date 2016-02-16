package io.taig.bsts.report.ops

import cats.data.Xor
import cats.data.Xor._
import io.taig.bsts.Term
import io.taig.bsts.ops.Computed
import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.report.ReportableError
import shapeless.{ ::, HList, HNil }

trait nestedEvaluation {
    implicit def termReportableError[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, Xor[ReportableError[N, A], O]] :: HNil] {
            override type Out0 = Xor[ReportableError[N, A], O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, Xor[ReportableError[N, A], O]] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Right( output ) ⇒ ( Some( output ), Computed( v :: HNil ) )
                    case i @ Left( _ )       ⇒ ( None, Computed( i :: HNil ) )
                }
            }
        }
    }
}

object nestedEvaluation extends nestedEvaluation