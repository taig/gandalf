package io.taig.gandalf.typelevel

trait Transformation extends Mutation

object Transformation {
    type In[I] = Mutation { type Input = I }

    type Out[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }
}