package io.taig.gandalf.predef.string

import io.taig.gandalf.core.{ Condition, Reportable }
import io.taig.gandalf.core.Rule.Applyable

final class matches[T <: String: ValueOf]
        extends Condition.With[String]( _.matches( valueOf[T] ) )
        with Reportable.With[String] {
    override def arguments( input: String ) = ( input, valueOf[T] )
}

object matches {
    def apply( value: String ): matches[value.type] = new matches[value.type]

    implicit def implicits[T <: String: ValueOf] = {
        Applyable.implicits[matches[T]]( new matches[T] )
    }
}