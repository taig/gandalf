package io.taig.gandalf

import cats.data.Validated._

trait Rule extends Validatable with Applyable with Symmetric {
    override final def apply( input: Input )( implicit e: Error[this.type] ) = {
        check( input ) match {
            case true ⇒ valid( input )
            case false ⇒
                val errors = e.show( arguments( input ) )
                invalid( errors )
        }
    }

    def check( input: Input ): Boolean
}

object Rule {
    type Aux[T] = Rule { type Input = T }
}