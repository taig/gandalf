package io.taig.gandalf

import cats.data.NonEmptyList
import shapeless.HNil
import shapeless.record.Record
import shapeless.syntax.singleton._

trait Error[-V <: Action with Arguments] {
    def error( arguments: V#Arguments ): NonEmptyList[String]
}

object Error {
    type Input[V <: Action with Arguments] = Record.`"input" -> V#Input`.T

    def input[V <: Action with Arguments]( input: V#Input ) = "input" ->> input :: HNil

    type Expectation[V <: Action with Arguments, E] = Record.`"input" -> V#Input, "expected" -> E`.T

    def expectation[V <: Action with Arguments, E]( input: V#Input, expected: E ) = {
        "input" ->> input :: "expected" ->> expected :: HNil
    }

    type Forward[V <: Action with Arguments] = Record.`"input" -> V#Input, "errors" -> NonEmptyList[String]`.T

    def forward[V <: Action with Arguments]( input: V#Input, errors: NonEmptyList[String] ) = {
        "input" ->> input :: "errors" ->> errors :: HNil
    }

    @inline
    def apply[V <: Action with Arguments: Error]: Error[V] = implicitly[Error[V]]

    def instance[V <: Action with Arguments]( message: String ): Error[V] = new Error[V] {
        override def error( arguments: V#Arguments ) = NonEmptyList( message )
    }

    def instance[V <: Action with Arguments]( f: V#Arguments ⇒ String ): Error[V] = new Error[V] {
        override def error( arguments: V#Arguments ) = NonEmptyList( f( arguments ) )
    }
}