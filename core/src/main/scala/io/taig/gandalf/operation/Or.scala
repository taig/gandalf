package io.taig.gandalf.operation

import cats.data.Validated._
import cats.std.list._
import cats.syntax.semigroup._
import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class Or[L <: Rule, R <: Rule.Aux[L#Input]] extends Operation[L, R]

object Or {
    implicit def validation[T, L <: Rule.Aux[T], R <: Rule.Aux[T]](
        implicit
        l: Validation[T, L],
        r: Validation[T, R]
    ): Validation[T, L || R] = {
        Validation.instance { input ⇒
            l.validate( input ) match {
                case Valid( output ) ⇒ valid( output )
                case Invalid( errorsLeft ) ⇒ r.validate( input ) match {
                    case Valid( output )        ⇒ valid( output )
                    case Invalid( errorsRight ) ⇒ invalid( errorsLeft |+| errorsRight )
                }
            }
        }
    }
}