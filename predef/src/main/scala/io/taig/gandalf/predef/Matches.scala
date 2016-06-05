package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule, Validation }
import shapeless._

import scala.language.existentials

final class Matches[T, I >: T] extends Rule {
    override type Input = I

    override type Arguments = Error.Expectation[Matches[T, I], T]
}

object Matches {
    implicit def validation[T, I >: T]( implicit w: Witness.Aux[T] ): Validation[I, Matches[T, I]] = {
        Validation.rule { input ⇒
            input == w.value match {
                case true  ⇒ valid( input )
                case false ⇒ invalidNel( "Matches" )
            }
        }
    }

    def apply[T, I >: T]( compare: Witness.Aux[T] ): Matches[T, I] = new Matches[T, I]
}