package io.taig.bsts.tests

import io.taig.bsts.rule._
import shapeless._
import shapeless.syntax.singleton._

class RuleTest extends Suite {
    "apply" should "allow to create simple rules" in {
        val builder = Rule[String]( "required" )

        builder shouldBe a[Rule.Builder1[_, _]]

        val rule = builder( _.nonEmpty )

        rule shouldBe a[Rule[_, _, _]]
        rule.validate( "foo" ).isRight shouldBe true
        rule.validate( "" ).isRight shouldBe false
    }

    it should "allow to create rules with validation arguments" in {
        val builder = Rule[String]( "match" )

        builder shouldBe a[Rule.Builder1[_, _]]

        val rule = ( compare: String ) ⇒ builder( _ == compare ) { value ⇒
            "expected" ->> value :: HNil
        }

        rule( "foo" ) shouldBe a[Rule[_, _, _]]
        rule( "foo" ).validate( "foo" ).isRight shouldBe true
        rule( "foo" ).validate( "" ).isRight shouldBe false
    }

    it should "allow to create simple rules with transformations" in {
        val builder = Rule[String, Int]( "min" )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _.length )( _ >= length )

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
        rule( 3 ).validate( "foobar" ).isRight shouldBe true
        rule( 3 ).validate( "" ).isRight shouldBe false
    }

    it should "allow to create simple rules with transformations and validation arguments" in {
        val builder = Rule[String, Int]( "min" )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _.length )( _ >= length ) { ( value, actual ) ⇒
            "expected" ->> length :: HNil
        }

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
        rule( 3 ).validate( "foobar" ).isRight shouldBe true
        rule( 3 ).validate( "" ).isRight shouldBe false
    }

    "matches" should "verify that the validation value equals the expectation" in {
        generic.matches( "foobar" ).validate( "foobar" ).isRight shouldBe true
        generic.matches( "" ).validate( "" ).isRight shouldBe true
        generic.matches( "foobar" ).validate( "foo" ).isRight shouldBe false

        generic.matches( 5 ).validate( 5 ).isRight shouldBe true
        generic.matches( 5 ).validate( 42 ).isRight shouldBe false
    }

    "required" should "verify that a String is not empty" in {
        string.required.validate( "foo" ).isRight shouldBe true
        string.required.validate( "" ).isRight shouldBe false
    }

    "eq" should "verify that the validation value equals the expectation" in {
        numeric.eq( 5 ).validate( 5 ).isRight shouldBe true
        numeric.eq( 5 ).validate( 42 ).isRight shouldBe false
    }

    "gt" should "verify that the validation value is greater than the expectation" in {
        numeric.gt( 5 ).validate( 42 ).isRight shouldBe true
        numeric.gt( 5 ).validate( 5 ).isRight shouldBe false
        numeric.gt( 5 ).validate( 4 ).isRight shouldBe false
    }

    "gte" should "verify that the validation value is greater than or equal to the expectation" in {
        numeric.gte( 5 ).validate( 42 ).isRight shouldBe true
        numeric.gte( 5 ).validate( 5 ).isRight shouldBe true
        numeric.gte( 5 ).validate( 4 ).isRight shouldBe false
    }

    "lt" should "verify that the validation value is lesser than the expectation" in {
        numeric.lt( 5 ).validate( 4 ).isRight shouldBe true
        numeric.lt( 5 ).validate( 5 ).isRight shouldBe false
        numeric.lt( 5 ).validate( 42 ).isRight shouldBe false
    }

    "lte" should "verify that the validation value is lesser than or equal to the expectation" in {
        numeric.lte( 5 ).validate( 4 ).isRight shouldBe true
        numeric.lte( 5 ).validate( 5 ).isRight shouldBe true
        numeric.lte( 5 ).validate( 42 ).isRight shouldBe false
    }

    "email" should "verify that the validation value is an email address" in {
        string.email.validate( "mail@example.org" ).isRight shouldBe true
        string.email.validate( "" ).isRight shouldBe false
        string.email.validate( "foobar" ).isRight shouldBe false
        string.email.validate( "foobar@" ).isRight shouldBe false
        string.email.validate( "foobar@example" ).isRight shouldBe false
    }

    "exactly" should "verify that the validation value has the expected length" in {
        string.exactly( 6 ).validate( "foobar" ).isRight shouldBe true
        string.exactly( 0 ).validate( "" ).isRight shouldBe true
        string.exactly( 6 ).validate( "foo" ).isRight shouldBe false
        string.exactly( 6 ).validate( "" ).isRight shouldBe false
    }

    "max" should "verify that the validation value is shorter than the expected length" in {
        string.max( 6 ).validate( "foo" ).isRight shouldBe true
        string.max( 6 ).validate( "foobar" ).isRight shouldBe true
        string.max( 0 ).validate( "" ).isRight shouldBe true
        string.max( 6 ).validate( "foobar42" ).isRight shouldBe false
    }

    "min" should "verify that the validation value is longer than the expected length" in {
        string.min( 6 ).validate( "foobar42" ).isRight shouldBe true
        string.min( 6 ).validate( "foobar" ).isRight shouldBe true
        string.min( 0 ).validate( "" ).isRight shouldBe true
        string.min( 6 ).validate( "foo" ).isRight shouldBe false
    }

    "phone" should "verify that the validation value is a phone number" in {
        string.phone.validate( "123456" ).isRight shouldBe true
        string.phone.validate( "+49123456" ).isRight shouldBe true
        string.phone.validate( "" ).isRight shouldBe false
        string.phone.validate( "42foobar" ).isRight shouldBe false
        string.phone.validate( "foobar42" ).isRight shouldBe false
        string.phone.validate( "foobar42example" ).isRight shouldBe false
    }
}