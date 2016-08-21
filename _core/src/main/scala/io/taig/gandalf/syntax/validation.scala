package io.taig.gandalf.syntax

import io.taig.gandalf._
import io.taig.gandalf.data.Action

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[I, O, A <: Validation.Aux[I, O]](
        action: A with Action.Aux[I, O]
    ): operation.validation[I, O, A] = {
        new operation.validation[I, O, A]( action )
    }
}

object validation extends validation
