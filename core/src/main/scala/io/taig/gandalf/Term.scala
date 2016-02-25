package io.taig.gandalf

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.gandalf.ops.NestedEvaluation
import shapeless._

abstract class Term[N <: String, I, O, A <: HList]( implicit w: Witness.Aux[N] ) extends Validation[I, O] {
    def name: String = w.value

    override def toString = name
}

object Term {
    type Aux[N <: String, I, O, A <: HList, E0] = Term[N, I, O, A] { type E = E0 }

    implicit def nestedEvaluationTerm[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Term.Aux[N, I, O, A, Error[N, A]] :: HNil] {
            override type Out0 = Validated[Error[N, A], O] :: HNil

            override def apply( input: I, tree: Term.Aux[N, I, O, A, Error[N, A]] :: HNil ) = tree match {
                case term :: HNil ⇒ term.validate( input ) match {
                    case v @ Valid( output ) ⇒ ( Some( output ), v :: HNil )
                    case i @ Invalid( _ )    ⇒ ( None, i :: HNil )
                }
            }
        }
    }
}