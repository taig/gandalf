package io.taig.gandalf.predef

import io.taig.gandalf.data.Rule
import io.taig.gandalf.{ Error, Validation }

sealed trait Required extends Rule {
    override type Input = String

    override type Arguments = Error.Input[Required]
}

object Required extends Required {
    implicit def validation(
        implicit
        e: Error[Required]
    ): Validation[String, Required] = {
        Validation.rule[String, Required]( _.nonEmpty )( Error.input[Required] )
    }
}