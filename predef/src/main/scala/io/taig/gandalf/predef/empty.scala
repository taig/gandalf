package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class empty extends Rule.Condition

object empty extends empty {
    implicit val string: Validation.Aux[empty, String, String] =
        Validation.condition( _.isEmpty )

    implicit def traversable[T <: Traversable[_]]: Validation.Aux[empty, T, T] =
        Validation.condition( _.isEmpty )

    implicit def array[T]: Validation.Aux[empty, Array[T], Array[T]] =
        Validation.condition( _.length == 0 )
}