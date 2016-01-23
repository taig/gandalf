package io.taig.bsts.rules

import io.taig.bsts.Suite

class NumericTest extends Suite {
    "eq" should "verify that the validation value equals the expectation" in {
        numeric.eq( 5 ).validate( 5 ).isSuccess shouldBe true
        numeric.eq( 5 ).validate( 42 ).isSuccess shouldBe false
    }

    "gt" should "verify that the validation value is greater than the expectation" in {
        numeric.gt( 5 ).validate( 42 ).isSuccess shouldBe true
        numeric.gt( 5 ).validate( 5 ).isSuccess shouldBe false
        numeric.gt( 5 ).validate( 4 ).isSuccess shouldBe false
    }

    "gte" should "verify that the validation value is greater than or equal to the expectation" in {
        numeric.gte( 5 ).validate( 42 ).isSuccess shouldBe true
        numeric.gte( 5 ).validate( 5 ).isSuccess shouldBe true
        numeric.gte( 5 ).validate( 4 ).isSuccess shouldBe false
    }

    "lt" should "verify that the validation value is lesser than the expectation" in {
        numeric.lt( 5 ).validate( 4 ).isSuccess shouldBe true
        numeric.lt( 5 ).validate( 5 ).isSuccess shouldBe false
        numeric.lt( 5 ).validate( 42 ).isSuccess shouldBe false
    }

    "lte" should "verify that the validation value is lesser than or equal to the expectation" in {
        numeric.lte( 5 ).validate( 4 ).isSuccess shouldBe true
        numeric.lte( 5 ).validate( 5 ).isSuccess shouldBe true
        numeric.lte( 5 ).validate( 42 ).isSuccess shouldBe false
    }
}