package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

import scala.Ordering.Implicits._

class gt[T: ValueOf] extends Rule.Condition

object gt {
    def apply[T]( value: T ): gt[value.type] = new gt[value.type]

    implicit def numeric[T <: U: ValueOf, U: Numeric]: Validation[gt[T], U, U] =
        Validation.condition( _ > valueOf[T] )
}