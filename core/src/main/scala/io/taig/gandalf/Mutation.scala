package io.taig.gandalf

trait Mutation extends Validatable

object Mutation {
    type Input[I] = Mutation { type Input = I }

    type Output[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }
}