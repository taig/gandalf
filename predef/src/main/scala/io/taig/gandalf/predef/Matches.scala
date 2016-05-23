package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Rule }
import shapeless._
import shapeless.syntax.singleton._

import scala.language.existentials

abstract class Matches[T, I >: T] extends Rule {
    override type Input = I

    override type Arguments = Error.Expectation[Matches[T, I], T]
}

object Matches {
    implicit val error = Error.instance[Matches[_, _]]( "matches" )

    implicit def evaluation[T, I >: T]( implicit w: Witness.Aux[T] ) = {
        Evaluation.rule[Matches[T, I]]( _ == w.value ) { input ⇒
            "input" ->> input :: "expected" ->> w.value :: HNil
        }
    }

    def matches[T]( wit: Witness.Aux[T] )( implicit wid: Widen[T] ): Matches[T, wid.Out] = new Matches[T, wid.Out] {}
}