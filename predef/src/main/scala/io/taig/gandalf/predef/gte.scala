package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

import scala.Ordering.Implicits._

class gte[T: ValueOf] extends Rule.Condition

object gte {
    def apply[T]( value: T ): gte[value.type] = new gte[value.type]

    implicit def numeric[T <: U: ValueOf, U: Numeric]: Validation[gte[T], U, U] =
        Validation.condition( _ >= valueOf[T] )
}