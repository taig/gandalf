package io.taig.gandalf.data

import io.taig.gandalf._

trait Operation[L <: Action, R <: Action.Input[L#Output]]
        extends Action
        with Arguments {
    override final type Input = L#Input

    override final type Output = R#Output

    override final type Arguments = Error.Forward[this.type]
}