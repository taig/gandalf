package io.taig.gandalf.macros

import io.taig.gandalf.core.Rule

class InferenceHelper[I] {
    def infer[R <: Rule.Input[I]]( rule: R with Rule.Input[I] ): R = rule
}