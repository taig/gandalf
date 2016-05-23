package io.taig.gandalf

import cats.data.NonEmptyList
import shapeless.record.Record

trait Error[-V <: Validation] {
    def error( arguments: V#Arguments ): NonEmptyList[String]
}

object Error {
    type Input[V <: Validation] = Record.`"input" -> V#Input`.T

    type Expectation[V <: Validation, E] = Record.`"input" -> V#Input, "expected" -> E`.T

    type Forward[V <: Validation] = Record.`"input" -> V#Input, "errors" -> NonEmptyList[String]`.T

    @inline
    def apply[V <: Validation: Error]: Error[V] = implicitly[Error[V]]

    def instance[V <: Validation]( message: String ): Error[V] = new Error[V] {
        override def error( arguments: V#Arguments ) = NonEmptyList( message )
    }

    def instance[V <: Validation]( f: V#Arguments ⇒ String ): Error[V] = new Error[V] {
        override def error( arguments: V#Arguments ) = NonEmptyList( f( arguments ) )
    }
}