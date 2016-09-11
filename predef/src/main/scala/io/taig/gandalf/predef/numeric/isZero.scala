package io.taig.gandalf.predef.numeric

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core.{ Arguments, Condition }

class isZero[T: Numeric]
    extends Condition.With[T]( _ == zero )
    with Arguments.Input

object isZero {
    def apply[T: Numeric]: isZero[T] = new isZero[T]

    implicit def implicits[T: Numeric] = {
        Applyable.implicits[isZero[T]]( new isZero[T] )
    }
}