package io.taig.bsts.rules

import io.taig.bsts.Validation$
import shapeless._
import shapeless.syntax.singleton._

trait generic {
    def matches[T]( expected: T ) = Validation[T]( "matches" )( _ == expected ) {
        "value" ->> _ :: "expected" ->> expected :: HNil
    }
}

object generic extends generic