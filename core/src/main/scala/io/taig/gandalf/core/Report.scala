package io.taig.gandalf.core

import cats.data.NonEmptyList

trait Report[-C <: Container] {
    def show( arguments: C#Kind#Arguments ): NonEmptyList[String]
}

object Report {
    @inline
    def apply[C <: Container]( implicit r: Report[C] ): Report[C] = r

    def instance[C <: Container](
        f: C#Kind#Arguments ⇒ NonEmptyList[String]
    ): Report[C] = new Report[C] {
        override def show( arguments: C#Kind#Arguments ) = f( arguments )
    }

    def one[C <: Container](
        f: C#Kind#Arguments ⇒ String
    ): Report[C] = instance( arguments ⇒ NonEmptyList.of( f( arguments ) ) )

    def static[C <: Container](
        value: String
    ): Report[C] = instance( _ ⇒ NonEmptyList.of( value ) )
}