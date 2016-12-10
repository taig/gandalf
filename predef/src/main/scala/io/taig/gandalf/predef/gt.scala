package io.taig.gandalf.predef

import io.taig.gandalf.core.Rule

class gt[T: ValueOf] extends Rule.Condition

object gt extends NumericComparison[gt]( _ > _ ) {
    def apply[T]( value: T ): gt[value.type] = new gt[value.type]
}