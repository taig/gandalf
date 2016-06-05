package io.taig.gandalf

import cats.data.Validated._

trait Transformation extends Action {
    override def validate( input: Input ) = valid( transform( input ) )

    def transform( input: Input ): Output
}

object Transformation {
    type In[I] = Transformation { type Input = I }

    type Out[O] = Transformation { type Output = O }

    type Aux[I, O] = Transformation { type Input = I; type Output = O }
}