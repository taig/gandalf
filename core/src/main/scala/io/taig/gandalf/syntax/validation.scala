package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[V <: Validation]( validation: V )(
        implicit
        ev: Evaluation[V]
    ): ops.validation[V] = new ops.validation[V]( validation )
}

object validation extends validation