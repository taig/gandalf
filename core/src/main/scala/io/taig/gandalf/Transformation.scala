package io.taig.gandalf

import cats.data.Validated._

trait Transformation extends Mutation with Applyable {
    override final type Arguments = Unit

    final def arguments( input: Input ): Arguments = {}

    override final def apply( input: Input )( implicit e: Error[this.type] ) = {
        valid( transform( input ) )
    }

    def transform( input: Input ): Output
}

object Transformation {
    type Input[I] = Transformation { type Input = I }

    type Output[O] = Transformation { type Output = O }

    type Aux[I, O] = Transformation { type Input = I; type Output = O }
}