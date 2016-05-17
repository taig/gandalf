package io.taig.gandalf.predef

import io.taig.gandalf.{ Evaluation, Transformation }

trait ToLowerCase extends Transformation {
    override type Input = String

    override type Output = String
}

object ToLowerCase {
    implicit val evaluation = Evaluation.transformation[ToLowerCase]( _.toLowerCase )

    val toLowerCase: Evaluation[ToLowerCase] = evaluation
}