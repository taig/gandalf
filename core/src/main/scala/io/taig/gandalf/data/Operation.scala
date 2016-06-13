package io.taig.gandalf.data

import io.taig.gandalf._

trait Operation
        extends Action
        with Arguments {
    type Left <: Action

    type Right <: Action.Input[Left#Output]

    override final type Input = Left#Input

    override final type Output = Right#Output

    override final type Arguments = Error.Forward[this.type]
}