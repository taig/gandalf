package io.taig.gandalf

trait Transformation extends Action {
    def transform( input: Input ): Output
}

object Transformation {
    type In[I] = Transformation { type Input = I }

    type Out[O] = Transformation { type Output = O }

    type Aux[I, O] = Transformation { type Input = I; type Output = O }
}