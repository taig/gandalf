package io.taig.gandalf.predef

import io.taig.gandalf.data.Rule
import io.taig.gandalf.{ Error, Validation }
import shapeless._

final class Regex[T <: String] extends Rule {
    override type Input = String

    override type Arguments = Error.Expectation[Regex[T]]
}

object Regex {
    implicit def validation[T <: String](
        implicit
        w: Witness.Aux[T],
        e: Error[Regex[T]]
    ) = Validation.rule[String, Regex[T]]( _ matches w.value )( Error.expectation[Regex[T]]( _, w.value ) )

    def apply[T <: String]( regex: Witness.Aux[T] ): Regex[T] = new Regex[T]
}