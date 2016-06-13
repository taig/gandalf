package io.taig.gandalf.data

import io.taig.gandalf._
import shapeless.record._

trait Operation
        extends Action
        with Arguments {
    type Left <: Action

    type Right <: Action.Input[Left#Output]

    override final type Input = Left#Input

    override final type Output = Right#Output

    override final type Arguments = Error.Forward[Operation]
}

object Operation {
    type Input[I] = Operation { type Output = I }

    type Output[O] = Operation { type Output = O }

    type Aux[I, O] = Input[I] with Output[I]

    implicit def errorOperation[O <: Operation]: Error[O] = new Error[O] {
        override def error( arguments: O#Arguments ) = arguments( "errors" )
    }
}