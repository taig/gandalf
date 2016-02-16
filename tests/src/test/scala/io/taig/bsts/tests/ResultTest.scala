package io.taig.bsts.tests

import cats.std.list._
import io.taig.bsts.syntax.validated._

class ResultTest extends Suite {
    it should "be possible to combine and accumulate validation results" in {
        ( rule.required.validate( "" ) |@| rule.required.validate( "foo" ) ) map {
            ( a, b ) ⇒ fail()
        }
    }
}