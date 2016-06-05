package io.taig.gandalf.syntax

import io.taig.gandalf._
import io.taig.gandalf.data.Action

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[O, A <: Action.Output[O]]( action: A with Action.Output[O] ): operation.validation[O, A] = {
        new operation.validation[O, A]( action )
    }
}

object validation extends validation
