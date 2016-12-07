package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

import scala.Ordering.Implicits._

class lt[T: ValueOf] extends Rule.Condition

object lt {
    def apply[T]( value: T ): lt[value.type] = new lt[value.type]

    implicit def numeric[T <: U: ValueOf, U: Numeric]: Validation[lt[T], U, U] =
        Validation.condition( _ < valueOf[T] )
}

