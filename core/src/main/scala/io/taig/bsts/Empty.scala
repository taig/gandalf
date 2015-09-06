package io.taig.bsts

import io.taig.bsts.rule.Required

/**
 * Type class to determine whether a value is empty or not
 */
trait Empty[-R <: Rule] {
    def isEmpty( value: R#Value ): Boolean
}

object Empty {
    def apply[R <: Rule]( empty: Boolean ): Empty[R] = new Empty[R] {
        override def isEmpty( value: R#Value ) = empty
    }

    def apply[R <: Rule]( empty: R#Value ⇒ Boolean ): Empty[R] = new Empty[R] {
        override def isEmpty( value: R#Value ) = empty( value )
    }

    implicit val `Empty[Rule]` = Empty[Rule.Aux[Any]]( false )

    implicit val `Empty[Rule[Array[_]]]` = Empty[Rule.Aux[Array[_]]]( ( array: Array[_] ) ⇒ array.isEmpty )

    implicit def `Empty[Rule[Array[T]]]`[T] = Empty[Rule.Aux[Array[T]]]( ( array: Array[T] ) ⇒ array.isEmpty )

    implicit def `Empty[Rule[Option]]`[T <: Option[_]] = Empty[Rule.Aux[T]]( ( option: T ) ⇒ option.isEmpty )

    implicit def `Empty[Rule[Iterable]]`[T <: Iterable[_]] = Empty[Rule.Aux[T]]( ( iterable: T ) ⇒ iterable.isEmpty )

    implicit def `Empty[Rule[String]]`[R <: Rule.Aux[String]] = Empty[R]( ( string: String ) ⇒ string.isEmpty )
}