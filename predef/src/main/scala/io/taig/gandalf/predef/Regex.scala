package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Rule }
import shapeless._
import shapeless.syntax.singleton._

trait Regex[T <: String] extends Rule {
    override type Input = String

    override type Arguments = Error.Expectation[Regex[T], T]
}

object Regex {
    implicit def evaluation[T <: String]( implicit w: Witness.Aux[T], e: Error[Regex[T]] ) = {
        Evaluation.rule[Regex[T]]( _ matches w.value ) { input ⇒
            "input" ->> input :: "expected" ->> w.value :: HNil
        }
    }

    def regex[T <: String]( regex: Witness.Aux[T] ): Regex[T] = new Regex[T] {}
}