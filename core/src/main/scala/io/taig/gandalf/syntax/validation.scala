package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[V <: Validatable](
        validatable: V
    ): operation.validation[V] = new operation.validation[V]
}

object validation extends validation