package io.taig.gandalf.internal

import io.taig.gandalf.{ Evaluation, Transformation }

trait Identity[T] extends Transformation {
    override type Input = T

    override type Output = T
}

object Identity {
    implicit def evaluation[T] = Evaluation.transformation[Identity[T]]( Predef.identity )

    implicit def identity[T]: Identity[T] = new Identity[T] {}
}