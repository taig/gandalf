package io.taig.gandalf

import io.taig.gandalf.syntax.aliases._

trait Action {
    type Input

    type Output

    def validate( input: Input ): Result[Output]

    override def toString = getClass.getSimpleName
}

object Action {
    type In[I] = Action { type Input = I }

    type Out[O] = Action { type Output = O }

    type Aux[I, O] = Action { type Input = I; type Output = O }
}