package io.taig.gandalf

import shapeless.HNil

trait Transformation extends Mutation {
    override final type Arguments = HNil
}

object Transformation {
    type In[I] = Mutation { type Input = I }

    type Out[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }
}