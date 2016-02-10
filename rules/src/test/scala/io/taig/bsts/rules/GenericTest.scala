package io.taig.bsts.rules

import io.taig.bsts.Suite

class GenericTest extends Suite {
    "matches" should "verify that the validation value equals the expectation" in {
        generic.matches( "foobar" ).validate( "foobar" ).isSuccess shouldBe true
        generic.matches( "" ).validate( "" ).isSuccess shouldBe true
        generic.matches( "foobar" ).validate( "foo" ).isSuccess shouldBe false

        generic.matches( 5 ).validate( 5 ).isSuccess shouldBe true
        generic.matches( 5 ).validate( 42 ).isSuccess shouldBe false
    }

    "required" should "verify that a String is not empty" in {
        generic.required[String].validate( "foo" ).isSuccess shouldBe true
        generic.required[String].validate( "" ).isSuccess shouldBe false
    }

    it should "verify that an Option is not empty" in {
        generic.required[Option[String]].validate( Some( "foo" ) ).isSuccess shouldBe true
        generic.required[Option[String]].validate( None ).isSuccess shouldBe false
    }
}