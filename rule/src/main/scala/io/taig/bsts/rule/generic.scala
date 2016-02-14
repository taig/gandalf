package io.taig.bsts.rule

import io.taig.bsts.rules.ops.Required
import shapeless._
import shapeless.syntax.singleton._

trait generic {
    def matches[T]( expected: T ) = Rule[T]( "matches" )( _ == expected ) {
        "value" ->> _ :: "expected" ->> expected :: HNil
    }

    def required[T: Required] = Rule[T]( "required" )( implicitly[Required[T]].exists )
}

object generic extends generic