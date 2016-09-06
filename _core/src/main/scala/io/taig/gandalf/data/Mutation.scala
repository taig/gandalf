package io.taig.gandalf.data

import io.taig.gandalf.Reportable$

trait Mutation extends Action with Reportable

object Mutation {
    type Input[I] = Mutation { type Input = I }

    type Output[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }
}