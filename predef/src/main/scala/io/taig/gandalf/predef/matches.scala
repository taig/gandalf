package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class matches[T <: String] extends Rule.Condition

object matches {
    def apply[T <: String]( value: T ): matches[value.type] =
        new matches[value.type]

    implicit def string[T <: String: ValueOf]: Validation.Aux[matches[T], String, String] =
        Validation.condition( _ matches valueOf[T] )
}