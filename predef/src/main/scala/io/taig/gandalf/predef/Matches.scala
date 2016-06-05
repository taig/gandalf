package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule }
import shapeless._

import scala.language.existentials

case class Matches[T, I >: T]( compare: Witness.Aux[T] ) extends Rule {
    override type Input = I

    override type Arguments = Error.Expectation[Matches[T, I], T]

    override def verify( input: Input ) = input == compare.value match {
        case true  ⇒ valid( input )
        case false ⇒ invalidNel( "Matches" )
    }
}