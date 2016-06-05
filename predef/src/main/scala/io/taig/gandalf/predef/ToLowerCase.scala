package io.taig.gandalf.predef

import io.taig.gandalf.Transformation

object ToLowerCase extends Transformation {
    override type Input = String

    override type Output = String

    override def transform( input: String ) = input.toLowerCase
}