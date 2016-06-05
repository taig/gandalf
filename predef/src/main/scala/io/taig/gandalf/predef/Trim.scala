package io.taig.gandalf.predef

import io.taig.gandalf.Transformation

class Trim extends Transformation {
    override type Input = String

    override type Output = String

    override def transform( input: String ) = input.trim
}

object Trim {
    val trim: Trim = new Trim
}