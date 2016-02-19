package io.taig.bsts.predef

import shapeless._
import shapeless.syntax.singleton._

trait generic {
    def identity[T] = Transformation[T, T]( "identity" )( Predef.identity )

    def matches[T]( expected: ⇒ T ) = Rule[T]( "matches" )( _ == expected ) {
        "value" ->> _ :: "expected" ->> expected :: HNil
    }
}

object generic extends generic