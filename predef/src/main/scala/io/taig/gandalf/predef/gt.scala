package io.taig.gandalf.predef

import io.taig.gandalf._

class gt[T: ValueOf] extends Rule.Condition

object gt extends NumericComparison[gt]( _ > _ ) {
    def apply[T]( value: T ): gt[value.type] = new gt[value.type]
}