package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Rule }

trait Required extends Rule {
    override type Input = String
}

object Required {
    implicit val error = Error.instance[Required]( "required" )

    implicit val evaluation = Evaluation.rule[Required]( _.nonEmpty )

    val required: Evaluation[Required] = evaluation
}