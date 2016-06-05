package io.taig.gandalf.data

trait Transformation extends Action

object Transformation {
    type Input[I] = Transformation { type Input = I }

    type Output[O] = Transformation { type Output = O }

    type Aux[I, O] = Input[I] with Output[O]
}