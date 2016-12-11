package io.taig.gandalf.predef

import io.taig.gandalf._

class equal[T: ValueOf] extends Rule.Condition

object equal {
    def apply[T]( value: T ): equal[value.type] = new equal[value.type]

    implicit def generic[T: ValueOf, U]: Validation[equal[T], U, U] =
        Validation.condition( _ == valueOf[T] )
}