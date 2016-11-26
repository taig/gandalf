package io.taig.gandalf.predef.length

import io.taig.gandalf.core.{ Rule, Validation }

class max[T <: Int] extends Rule.Condition

object max {
    def apply( value: Int ): max[value.type] = new max[value.type]

    implicit def string[T <: Int: ValueOf]: Validation[max[T], String, String] =
        Validation.condition( _.length <= valueOf[T] )

    implicit def traversable[T <: Int: ValueOf, S <: Traversable[_]]: Validation[max[T], S, S] =
        Validation.condition( _.size <= valueOf[T] )

    implicit def array[T <: Int: ValueOf, S]: Validation[max[T], Array[S], Array[S]] =
        Validation.condition( _.length <= valueOf[T] )
}