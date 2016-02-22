package io.taig.bsts.predef

import io.taig.bsts.Rule
import shapeless._
import shapeless.syntax.singleton._

trait generic {
    def matches[T]( expected: ⇒ T ) = Rule[T]( "matches" )( _ == expected ) {
        "value" ->> _ :: "expected" ->> expected :: HNil
    }
}

object generic extends generic