package io.taig.gandalf.predef

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core._

trait generic {
    final class equal[T <: U: ValueOf, U]
            extends Condition.With[U]( _ == valueOf[T] )
            with Reportable.With[T] {
        override def arguments( input: Input ) = ( input, valueOf[T] )
    }

    object equal {
        def apply[T]( value: T ): equal[value.type, T] = {
            new equal[value.type, T]
        }

        implicit def implicits[T <: U: ValueOf, U] = {
            Applyable.implicits[equal[T, U]]( new equal[T, U] )
        }
    }
}

object generic extends generic