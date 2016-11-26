package io.taig.gandalf.predef.length

import io.taig.gandalf.core.{ Rule, Validation }

class min[T <: Int] extends Rule.Condition

object min {
    def apply( value: Int ): min[value.type] = new min[value.type]

    implicit def string[T <: Int: ValueOf]: Validation[min[T], String, String] =
        Validation.condition( _.length >= valueOf[T] )

    implicit def traversable[T <: Int: ValueOf, S <: Traversable[_]]: Validation[min[T], S, S] =
        Validation.condition( _.size >= valueOf[T] )

    implicit def array[T <: Int: ValueOf, S]: Validation[min[T], Array[S], Array[S]] =
        Validation.condition( _.length >= valueOf[T] )
}