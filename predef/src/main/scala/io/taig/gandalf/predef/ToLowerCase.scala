package io.taig.gandalf.predef

import io.taig.gandalf.{ Transformation, Validation }

sealed trait ToLowerCase extends Transformation {
    override type Input = String

    override type Output = String
}

object ToLowerCase extends ToLowerCase {
    implicit val validation: Validation[String, ToLowerCase] = Validation.transformation( _.toLowerCase )
}