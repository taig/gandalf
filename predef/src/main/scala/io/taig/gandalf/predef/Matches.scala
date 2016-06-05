package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule }
import shapeless._

import scala.language.existentials

class Matches[T, I >: T]( implicit w: Witness.Aux[T] ) extends Rule {
    override type Input = I

    override type Arguments = Error.Expectation[Matches[T, I], T]

    override def verify( input: I ) = input == w.value match {
        case true  ⇒ valid( input )
        case false ⇒ invalidNel( "Matches" )
    }
}

object Matches {
    def matches[T]( compare: Witness.Aux[T] )( implicit wid: Widen[T] ): Matches[T, wid.Out] = {
        new Matches[T, wid.Out]()( compare )
    }
}