package io.taig.gandalf.predef.numeric

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core.{ Arguments, Condition }

import scala.Ordering.Implicits._

class positive[T: Numeric]
    extends Condition.With[T]( _ > zero )
    with Arguments.Input

object positive {
    def apply[T: Numeric]: positive[T] = new positive[T]

    implicit def implicits[T: Numeric] = {
        Applyable.implicits[positive[T]]( positive[T] )
    }
}