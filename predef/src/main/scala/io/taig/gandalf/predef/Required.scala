package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule }

object Required extends Rule {
    override type Input = String

    override type Arguments = Error.Input[Required.type]

    override def verify( input: String ) = input.nonEmpty match {
        case true  ⇒ valid( input )
        case false ⇒ invalidNel( "Required" )
    }
}