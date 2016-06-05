package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule, Validation }
import shapeless.Witness

final class Regex[T <: String] extends Rule {
    override type Input = String

    override type Arguments = Error.Expectation[Regex[T], T]
}

object Regex {
    implicit def validation[T <: String]( implicit w: Witness.Aux[T] ): Validation[String, Regex[T]] = {
        Validation.rule { input ⇒
            input matches w.value match {
                case true  ⇒ valid( input )
                case false ⇒ invalidNel( "Regex" )
            }
        }
    }

    def apply[T <: String]( regex: Witness.Aux[T] ): Regex[T] = new Regex[T]
}