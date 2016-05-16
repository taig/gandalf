package io.taig.gandalf.typelevel

//@annotation.Rule[String]
trait Required extends Rule {
    override type Input = String
}

object Required {
    implicit val error = Error.instance[Required]( "required" )

    implicit val evaluation = Evaluation.rule[Required]( _.nonEmpty )

    val required: Evaluation[Required] = evaluation
}