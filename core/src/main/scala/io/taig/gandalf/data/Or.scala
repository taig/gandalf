package io.taig.gandalf.data

import cats.data.Validated._
import cats.std.list._
import cats.syntax.semigroup._
import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class Or extends Operation {
    override type Left <: Rule

    override type Right <: Rule.Aux[Left#Output]
}

object Or {
    type Aux[L <: Rule, R <: Rule.Aux[L#Output]] = Or { type Left = L; type Right = R }

    implicit def validation[T, O <: Or { type Left <: Rule.Aux[T]; type Right <: Rule.Aux[T] }](
        implicit
        l: Validation[T, O#Left],
        r: Validation[T, O#Right],
        e: Error[O]
    ) = {
        Validation.operation[T, O] { input ⇒
            l.validate( input ) match {
                case Valid( output ) ⇒ valid( output )
                case Invalid( errorsLeft ) ⇒ r.validate( input ) match {
                    case Valid( output )        ⇒ valid( output )
                    case Invalid( errorsRight ) ⇒ invalid( errorsLeft |+| errorsRight )
                }
            }
        } {
            Error.forward[O]( _, _ )
        }
    }
}