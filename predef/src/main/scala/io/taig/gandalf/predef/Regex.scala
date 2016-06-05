package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule }
import shapeless.Witness

class Regex[T <: String]( implicit w: Witness.Aux[T] ) extends Rule {
    override type Input = String

    override type Arguments = Error.Expectation[Regex[T], T]

    override def verify( input: String ) = input matches w.value match {
        case true  ⇒ valid( input )
        case false ⇒ invalidNel( "Regex" )
    }
}

object Regex {
    def regex[T <: String]( regex: Witness.Aux[T] ): Regex[T] = new Regex[T]()( regex )
}