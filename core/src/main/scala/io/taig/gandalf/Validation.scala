package io.taig.gandalf

import cats.data.Validated._
import io.taig.gandalf.syntax.aliases._

trait Validation[O, -A <: Action.Output[O]] {
    def validate( input: A#Input ): Result[O]
}

object Validation {
    @inline
    def apply[O, A <: Action.Output[O]]( implicit v: Validation[O, A] ): Validation[O, A] = v

    def instance[O, A <: Action.Output[O]]( f: A#Input ⇒ Result[O] ): Validation[O, A] = new Validation[O, A] {
        override def validate( input: A#Input ) = f( input )
    }

    def mutation[O, M <: Mutation.Output[O]]( f: M#Input ⇒ Result[O] ): Validation[O, M] = {
        new Validation[O, M] {
            override def validate( input: M#Input ) = f( input )
        }
    }

    def rule[T, R <: Rule.Aux[T]]( f: T ⇒ Result[T] ): Validation[T, R] = {
        new Validation[T, R] {
            override def validate( input: R#Input ) = f( input )
        }
    }

    def transformation[O, T <: Transformation.Output[O]]( f: T#Input ⇒ O ): Validation[O, T] = {
        new Validation[O, T] {
            override def validate( input: T#Input ) = valid( f( input ) )
        }
    }
}