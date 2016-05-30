package io.taig.gandalf

import cats.data.NonEmptyList
import shapeless.HNil
import shapeless.record.Record
import shapeless.syntax.singleton._

trait Error[-V <: Validation] {
    def error( arguments: V#Arguments ): NonEmptyList[String]
}

object Error {
    type Input[V <: Validation] = Record.`"input" -> V#Input`.T

    def input[V <: Validation]( input: V#Input ) = "input" ->> input :: HNil

    type Expectation[V <: Validation, E] = Record.`"input" -> V#Input, "expected" -> E`.T

    def expectation[V <: Validation, E]( input: V#Input, expected: E ) = {
        "input" ->> input :: "expected" ->> expected :: HNil
    }

    type Forward[V <: Validation] = Record.`"input" -> V#Input, "errors" -> NonEmptyList[String]`.T

    def forward[V <: Validation]( input: V#Input, errors: NonEmptyList[String] ) = {
        "input" ->> input :: "errors" ->> errors :: HNil
    }

    @inline
    def apply[V <: Validation: Error]: Error[V] = implicitly[Error[V]]

    def instance[V <: Validation]( message: String ): Error[V] = new Error[V] {
        override def error( arguments: V#Arguments ) = NonEmptyList( message )
    }

    def instance[V <: Validation]( f: V#Arguments ⇒ String ): Error[V] = new Error[V] {
        override def error( arguments: V#Arguments ) = NonEmptyList( f( arguments ) )
    }
}