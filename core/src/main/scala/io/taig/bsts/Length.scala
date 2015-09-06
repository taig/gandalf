package io.taig.bsts

import scala.language.reflectiveCalls

trait Length[T] {
    def length( value: T ): Int
}

object Length {
    def apply[T]( f: T ⇒ Int ) = new Length[T] {
        override def length( value: T ) = f( value )
    }

    implicit val `Length[Array[_]]` = Length[Array[_]]( _.length )

    implicit def `Length[Array[T]]`[T] = Length[Array[T]]( _.length )

    implicit val `Length[String]` = Length[String]( _.length )

    implicit def `Length[Traversable]`[T <: Traversable[_]] = Length[T]( _.size )
}