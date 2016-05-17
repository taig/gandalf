package io.taig.gandalf

trait Validation {
    type Input

    type Output
}

object Validation {
    type In[I] = Validation { type Input = I }

    type Out[O] = Validation { type Output = O }

    type Aux[I, O] = Validation { type Input = I; type Output = O }
}