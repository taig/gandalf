package io.taig.gandalf.android.syntax

import io.taig.gandalf.Validation
import io.taig.gandalf.android.ops
import shapeless.HList

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[I, O, V <: HList, E](
        validation: Validation.Aux[I, O, V, E]
    ): ops.validation[I, O, V, E] = new ops.validation( validation )
}

object validation extends validation