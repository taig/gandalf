package io.taig.gandalf.typelevel

trait Trim extends Transformation {
    override type Input = String

    override type Output = String
}

object Trim {
    implicit val evaluation = Evaluation.transformation[Trim]( _.trim )

    val trim: Evaluation[Trim] = evaluation
}