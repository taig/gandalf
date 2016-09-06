package io.taig.gandalf

import cats.data.Validated._
import io.taig.gandalf.Rule.Arguments

trait Condition extends Rule with Arguments {
    override final type Output = Input
}

object Condition {
    type Aux[T] = Condition { type Input = T }

    trait Applyable[T]
            extends Condition
            with Rule.Applyable {
        override type Input = T

        override final def apply( input: T )(
            implicit
            e: Error[this.type]
        ): Result[this.type] = check( input ) match {
            case true  ⇒ valid( input )
            case false ⇒ invalid( e.show( arguments( input ) ) )
        }

        def check( input: T ): Boolean
    }

    abstract class With[T]( f: T ⇒ Boolean ) extends Applyable[T] {
        override final def check( input: T ) = f( input )
    }
}