package io.taig.bsts.rules

import io.taig.bsts.Suite

class StringTest extends Suite {
    "email" should "verify that the validation value is an email address" in {
        string.email.validate( "mail@example.org" ).isSuccess shouldBe true
        string.email.validate( "" ).isSuccess shouldBe false
        string.email.validate( "foobar" ).isSuccess shouldBe false
        string.email.validate( "foobar@" ).isSuccess shouldBe false
        string.email.validate( "foobar@example" ).isSuccess shouldBe false
    }

    "exactly" should "verify that the validation value has the expected length" in {
        string.exactly( 6 ).validate( "foobar" ).isSuccess shouldBe true
        string.exactly( 0 ).validate( "" ).isSuccess shouldBe true
        string.exactly( 6 ).validate( "foo" ).isSuccess shouldBe false
        string.exactly( 6 ).validate( "" ).isSuccess shouldBe false
    }

    "max" should "verify that the validation value is shorter than the expected length" in {
        string.max( 6 ).validate( "foo" ).isSuccess shouldBe true
        string.max( 6 ).validate( "foobar" ).isSuccess shouldBe true
        string.max( 0 ).validate( "" ).isSuccess shouldBe true
        string.max( 6 ).validate( "foobar42" ).isSuccess shouldBe false
    }

    "min" should "verify that the validation value is longer than the expected length" in {
        string.min( 6 ).validate( "foobar42" ).isSuccess shouldBe true
        string.min( 6 ).validate( "foobar" ).isSuccess shouldBe true
        string.min( 0 ).validate( "" ).isSuccess shouldBe true
        string.min( 6 ).validate( "foo" ).isSuccess shouldBe false
    }

    "phone" should "verify that the validation value is a phone number" in {
        string.phone.validate( "123456" ).isSuccess shouldBe true
        string.phone.validate( "+49123456" ).isSuccess shouldBe true
        string.phone.validate( "" ).isSuccess shouldBe false
        string.phone.validate( "42foobar" ).isSuccess shouldBe false
        string.phone.validate( "foobar42" ).isSuccess shouldBe false
        string.phone.validate( "foobar42example" ).isSuccess shouldBe false
    }
}