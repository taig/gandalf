package io.taig.gandalf.predef.numeric

import io.taig.gandalf.core.{ Condition, Reportable }
import io.taig.gandalf.core.Rule.Applyable
import scala.Ordering.Implicits._

class gte[T <: U: ValueOf, U: Numeric]
        extends Condition.With[U]( _ >= valueOf[T] )
        with Reportable.With[T] {
    override def arguments( input: U ) = ( input, valueOf[T] )
}

object gte {
    def apply[T: Numeric]( value: T ): gte[value.type, T] = new gte[value.type, T]

    implicit def implicits[T <: U: ValueOf, U: Numeric] = {
        Applyable.implicits[gte[T, U]]( new gte[T, U] )
    }
}