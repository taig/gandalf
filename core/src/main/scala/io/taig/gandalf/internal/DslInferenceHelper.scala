package io.taig.gandalf.internal

import io.taig.gandalf.data.Action

class DslInferenceHelper[T] {
    def infer[A <: Action.Input[T]]( action: A with Action.Input[T] ): A = action
}