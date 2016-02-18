package io.taig.bsts

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.ops.hlist.NestedEvaluation
import shapeless._

abstract class Term[N <: String, I, O, A <: HList]( implicit w: Witness.Aux[N] ) extends Validation[I, O] {
    def name: String = w.value

    override def toString = name
}

object Term {
    type Aux[N <: String, I, O, A <: HList, R0] = Term[N, I, O, A] { type R = R0 }

    implicit def nestedEvaluationTerm[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, Validated[Error[N, A], O]] :: HNil] {
            type R = Validated[Error[N, A], O]

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