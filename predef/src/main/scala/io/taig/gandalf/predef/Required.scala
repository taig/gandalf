package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Rule }
import shapeless._
import shapeless.syntax.singleton._

trait Required extends Rule {
    override type Input = String

    override type Arguments = Error.Input[Required]
}

object Required {
    implicit val error = Error.instance[Required]( "required" )

    implicit val evaluation = Evaluation.rule[Required]( _.nonEmpty ) { input ⇒
        "input" ->> input :: HNil
    }

    val required: Required = new Required {}
}