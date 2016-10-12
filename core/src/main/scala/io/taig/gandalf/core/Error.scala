package io.taig.gandalf.core

import cats.data.NonEmptyList

trait Error[-C <: Container] {
    def show( arguments: C#Kind#Arguments ): NonEmptyList[String]
}

object Error {
    @inline
    def apply[C <: Container]( implicit e: Error[C] ): Error[C] = e

    def instance[C <: Container](
        f: C#Kind#Arguments ⇒ NonEmptyList[String]
    ): Error[C] = new Error[C] {
        override def show( arguments: C#Kind#Arguments ) = f( arguments )
    }

    def one[C <: Container](
        f: C#Kind#Arguments ⇒ String
    ): Error[C] = instance( arguments ⇒ NonEmptyList.of( f( arguments ) ) )

    def static[C <: Container](
        value: String
    ): Error[C] = instance( _ ⇒ NonEmptyList.of( value ) )
}