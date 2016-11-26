package io.taig.gandalf.core.syntax

import io.taig.gandalf.core.operation

import scala.language.implicitConversions

trait validation {
    implicit def gandalfCoreValidation[I]( input: I ): operation.validation[I] =
        new operation.validation[I]( input )
}

object validation extends validation