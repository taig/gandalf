package io.taig.bsts.android.syntax

import io.taig.bsts.Validation
import io.taig.bsts.android.ops
import shapeless.HList

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[I, O, V <: HList, E](
        validation: Validation.Aux[I, O, V, E]
    ): ops.validation[I, O, V, E] = new ops.validation( validation )
}

object validation extends validation