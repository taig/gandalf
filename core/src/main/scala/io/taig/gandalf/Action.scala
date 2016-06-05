package io.taig.gandalf

trait Action {
    type Input

    type Output
}

object Action {
    type Input[I] = Action { type Input = I }

    type Output[O] = Action { type Output = O }

    type Aux[I, O] = Input[I] with Output[O]
}