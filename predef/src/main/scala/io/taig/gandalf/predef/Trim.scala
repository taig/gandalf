package io.taig.gandalf.predef

import io.taig.gandalf.Transformation

object Trim extends Transformation {
    override type Input = String

    override type Output = String

    override def transform( input: String ) = input.trim
}