package io.taig.gandalf.typelevel

trait ToLowerCase extends Transformation {
    override type Input = String

    override type Output = String
}

object ToLowerCase {
    implicit val evaluation = Evaluation.transformation[ToLowerCase]( _.toLowerCase )
}