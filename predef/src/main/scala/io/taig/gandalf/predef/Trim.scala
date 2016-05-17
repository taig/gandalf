package io.taig.gandalf.predef

import io.taig.gandalf.{ Evaluation, Transformation }

trait Trim extends Transformation {
    override type Input = String

    override type Output = String
}

object Trim {
    implicit val evaluation = Evaluation.transformation[Trim]( _.trim )

    val trim: Evaluation[Trim] = evaluation
}