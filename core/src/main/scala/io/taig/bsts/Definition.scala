package io.taig.bsts

import scala.language.higherKinds

/**
 * Type class to define the actual logic of a rule
 */
trait Definition[-R <: Rule] {
    def apply( value: R#Value, rule: R ): Boolean
}

object Definition {
    def apply[R <: Rule]( f: R#Value ⇒ Boolean ): Definition[R] = new Definition[R] {
        override def apply( value: R#Value, rule: R ) = f( value )
    }

    def apply[R <: Rule]( f: ( R#Value, R ) ⇒ Boolean ): Definition[R] = new Definition[R] {
        override def apply( value: R#Value, rule: R ) = f( value, rule )
    }
}