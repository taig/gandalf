package io.taig.gandalf.core

import cats.data.Validated._
import shapeless.{ HNil, Witness }

trait Transformation extends Mutation {
    override final type Arguments = HNil

    final def apply( input: Input ) = valid( transform( input ) )

    def transform( input: Input ): Output
}

object Transformation {
    type Input[I] = Transformation { type Input = I }

    type Output[O] = Transformation { type Output = O }

    type Aux[I, O] = Transformation { type Input = I; type Output = O }

    abstract class With[I, O]( f: I ⇒ O ) extends Transformation {
        override final type Input = I

        override final type Output = O

        override final def transform( input: Input ) = f( input )
    }

    implicit def validation[T <: Transformation](
        implicit
        w: Witness.Aux[T]
    ): Validation[T] = Validation.instance[T] { input ⇒
        w.value.apply( input.asInstanceOf[w.value.Input] )
    }
}