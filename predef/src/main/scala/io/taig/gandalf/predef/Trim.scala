package io.taig.gandalf.predef

import io.taig.gandalf.{ Transformation, Validation }

sealed trait Trim extends Transformation {
    override type Input = String

    override type Output = String
}

object Trim extends Trim {
    implicit val validation: Validation[String, Trim] = Validation.transformation( _.trim )
}