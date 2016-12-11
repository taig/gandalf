package io.taig.gandalf.predef

import io.taig.gandalf._

object length {
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
}
