package io.taig.gandalf.predef.numeric

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core.{ Arguments, Condition }

import scala.Ordering.Implicits._

class gt[T <: U: ValueOf, U: Numeric]
        extends Condition.With[U]( _ > valueOf[T] )
        with Arguments.With[T] {
    override val argument = valueOf[T]
}

object gt {
    def apply[T: Numeric]( value: T ): gt[value.type, T] = new gt[value.type, T]

    implicit def implicits[T <: U: ValueOf, U: Numeric] = {
        Applyable.implicits[gt[T, U]]( new gt[T, U] )
    }
}