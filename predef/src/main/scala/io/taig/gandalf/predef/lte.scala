package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

import scala.Ordering.Implicits._

class lte[T: ValueOf] extends Rule.Condition

object lte {
    def apply[T]( value: T ): lte[value.type] = new lte[value.type]

    implicit def numeric[T <: U: ValueOf, U: Numeric]: Validation[lte[T], U, U] =
        Validation.condition( _ <= valueOf[T] )
}