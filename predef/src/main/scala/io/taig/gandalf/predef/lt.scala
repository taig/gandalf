package io.taig.gandalf.predef

import io.taig.gandalf._

class lt[T: ValueOf] extends Rule.Condition

object lt extends NumericComparison[lt]( _ < _ ) {
    def apply[T]( value: T ): lt[value.type] = new lt[value.type]
}