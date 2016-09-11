package io.taig.gandalf.predef.string

import io.taig.gandalf.core.Rule.Applyable
import io.taig.gandalf.core.{ Arguments, Condition }

final class matches[T <: String: ValueOf]
        extends Condition.With[String]( _.matches( valueOf[T] ) )
        with Arguments.With[T] {
    override val argument = valueOf[T]
}

object matches {
    def apply( value: String ): matches[value.type] = new matches[value.type]

    implicit def implicits[T <: String: ValueOf] = {
        Applyable.implicits[matches[T]]( new matches[T] )
    }
}