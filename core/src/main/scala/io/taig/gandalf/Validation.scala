package io.taig.gandalf

import cats.data.Validated._
import io.taig.gandalf.operator.Operation
import io.taig.gandalf.syntax.aliases._

trait Validation[A <: Action] {
    def validate( action: A )( input: action.Input ): Result[A#Output]
}

object Validation {
    implicit def mutation[M <: Mutation]: Validation[M] = new Validation[M] {
        override def validate( mutation: M )( input: mutation.Input ) = {
            mutation.mutate( input )
        }
    }

    implicit def operation[O <: Operation[_, _]]: Validation[O] = new Validation[O] {
        override def validate( action: O )( input: action.Input ) = {
            action.apply( input )
        }
    }

    implicit def transformation[T <: Transformation]: Validation[T] = new Validation[T] {
        override def validate( transformation: T )( input: transformation.Input ) = {
            valid( transformation.transform( input ) )
        }
    }

    implicit def rule[R <: Rule]: Validation[R] = new Validation[R] {
        override def validate( rule: R )( input: rule.Input ) = {
            rule.verify( input )
        }
    }
}