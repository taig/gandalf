package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule }

class Required extends Rule {
    override type Input = String

    override type Arguments = Error.Input[Required]

    override def verify( input: String ) = input.nonEmpty match {
        case true  ⇒ valid( input )
        case false ⇒ invalidNel( "Required" )
    }
}

object Required {
    val required: Required = new Required
}