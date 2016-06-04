package io.taig.gandalf

import shapeless.HNil

trait Transformation extends Mutation {
    override final type Arguments = HNil
}

object Transformation {
    type In[I] = Transformation { type Input = I }

    type Out[O] = Transformation { type Output = O }

    type Aux[I, O] = Transformation { type Input = I; type Output = O }
}