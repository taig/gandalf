package io.taig.gandalf.predef

import cats.data.Validated._
import io.taig.gandalf.{ Error, Rule, Validation }

sealed trait Required extends Rule {
    override type Input = String

    override type Arguments = Error.Input[Required]
}

object Required extends Required {
    implicit val validation: Validation[String, Required] = Validation.rule { input ⇒
        input.nonEmpty match {
            case true  ⇒ valid( input )
            case false ⇒ invalidNel( "Required" )
        }
    }
}