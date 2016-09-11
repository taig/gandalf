package io.taig.gandalf.predef.numeric

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core.{ Arguments, Condition }

import scala.Ordering.Implicits._

class negative[T: Numeric]
    extends Condition.With[T]( _ < zero )
    with Arguments.Input

object negative {
    def apply[T: Numeric]: negative[T] = new negative[T]

    implicit def implicits[T: Numeric] = {
        Applyable.implicits[negative[T]]( negative[T] )
    }
}