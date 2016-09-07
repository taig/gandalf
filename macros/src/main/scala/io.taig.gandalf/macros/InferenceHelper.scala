package io.taig.gandalf.macros

import io.taig.gandalf.core.Rule

private[gandalf] class InferenceHelper[T] {
    def infer[R <: Rule.Input[T]]( rule: R with Rule.Input[T] ): R = rule
}