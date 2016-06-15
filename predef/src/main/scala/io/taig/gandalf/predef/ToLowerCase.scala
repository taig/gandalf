package io.taig.gandalf.predef

import io.taig.gandalf.Validation
import io.taig.gandalf.data.Transformation

class ToLowerCase extends Transformation {
    override type Input = String

    override type Output = String
}

object ToLowerCase extends ToLowerCase {
    implicit val validation: Validation[String, ToLowerCase] = Validation.transformation( _.toLowerCase )

    val toLowerCase: ToLowerCase = new ToLowerCase
}