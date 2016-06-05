package io.taig.gandalf

import io.taig.gandalf.syntax.aliases._

trait Mutation extends Action with Arguments {
    override final def validate( input: Input ) = mutate( input )

    def mutate( input: Input ): Result[Output]
}

object Mutation {
    type In[I] = Mutation { type Input = I }

    type Out[O] = Mutation { type Output = O }

    type Aux[I, O] = Mutation { type Input = I; type Output = O }
}