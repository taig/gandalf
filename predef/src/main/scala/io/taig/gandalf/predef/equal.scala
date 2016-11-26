package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class equal[T: ValueOf] extends Rule.Condition

object equal {
    def apply[T]( value: T ): equal[value.type] = new equal[value.type]

    implicit def generic[T <: U: ValueOf, U]: Validation[equal[T], U, U] =
        Validation.condition( _ == valueOf[T] )
}