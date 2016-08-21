package io.taig.gandalf.syntax

import io.taig.gandalf.{ Validatable, operation }

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[V <: Validatable](
        action: V
    ): operation.validation[V] = new operation.validation[V]
}

object validation extends validation