package io.taig.gandalf

trait Action {
    type Input

    type Output
}

object Action {
    type In[I] = Action { type Input = I }

    type Out[O] = Action { type Output = O }

    type Aux[I, O] = Action { type Input = I; type Output = O }
}