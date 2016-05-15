package io.taig.gandalf.typelevel

trait Mutation extends Validation

object Mutation {
    type In[I] = Mutation { type Input = I }

    type Out[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }
}