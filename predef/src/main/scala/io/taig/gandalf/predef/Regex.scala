package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule }
import shapeless.Witness

case class Regex[T <: String]( regex: Witness.Aux[T] ) extends Rule {
    override type Input = String

    override type Arguments = Error.Expectation[Regex[T], T]

    override def verify( input: String ) = input matches regex.value match {
        case true  ⇒ valid( input )
        case false ⇒ invalidNel( "Regex" )
    }
}