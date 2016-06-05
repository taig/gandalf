package io.taig.gandalf.data

import io.taig.gandalf.Arguments

trait Mutation extends Action with Arguments

object Mutation {
    type Input[I] = Mutation { type Input = I }

    type Output[O] = Mutation { type Output = O }

    type Aux[I, O] = Input[I] with Output[O]
}