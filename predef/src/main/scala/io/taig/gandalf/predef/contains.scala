package io.taig.gandalf.predef

import io.taig.gandalf._

import scala.collection.SeqLike

class contains[T: ValueOf] extends Rule.Condition

object contains {
    def apply[T]( value: T ): contains[value.type] = new contains[value.type]

    implicit def string[T <: String: ValueOf]: Validation[contains[T], String, String] =
        Validation.condition( _ contains valueOf[T] )

    implicit def seqLike[T: ValueOf, S <: SeqLike[_, _]]: Validation[contains[T], S, S] =
        Validation.condition( _ contains valueOf[T] )

    implicit def array[T: ValueOf, S]: Validation[contains[T], Array[S], Array[S]] =
        Validation.condition( _ contains valueOf[T] )
}