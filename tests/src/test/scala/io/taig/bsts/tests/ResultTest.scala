package io.taig.bsts.tests

import io.taig.bsts.rule.all._

class ResultTest extends Suite {
    it should "be possible to evaluate validation results in a for-comprehension" in {
        val result = for {
            required ← required.validate( "foo" )
            min ← min( 5 ).validate( "bar" )
            max ← max( 3 ).validate( "foobar" )
        } yield ( required, min, max )

        result.isLeft shouldBe true
    }
}