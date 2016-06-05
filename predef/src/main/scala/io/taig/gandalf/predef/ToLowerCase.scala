package io.taig.gandalf.predef

import io.taig.gandalf.Transformation

class ToLowerCase extends Transformation {
    override type Input = String

    override type Output = String

    override def transform( input: String ) = input.toLowerCase
}

object ToLowerCase {
    val toLowerCase: ToLowerCase = new ToLowerCase
}