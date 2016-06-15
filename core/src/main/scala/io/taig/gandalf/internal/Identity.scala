package io.taig.gandalf.internal

import io.taig.gandalf.Validation
import io.taig.gandalf.data.Transformation

final class Identity[T] extends Transformation {
    override type Input = T

    override type Output = T
}

object Identity {
    def apply[T]: Identity[T] = new Identity[T]

    implicit def validation[T] = Validation.transformation[T, Identity[T]]( Predef.identity )
}