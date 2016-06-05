package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[O, A <: Action.Output[O]]( action: A with Action.Output[O] ): ops.validation[O, A] = {
        new ops.validation[O, A]( action )
    }
}

object validation extends validation
