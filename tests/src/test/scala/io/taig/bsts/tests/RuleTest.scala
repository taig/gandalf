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
        rule.validate( "foo" ).isValid shouldBe true
        rule.validate( "" ).isValid shouldBe false
    }

    it should "allow to create rules with validation arguments" in {
        val builder = Rule[String]( "match" )

        builder shouldBe a[Rule.Builder1[_, _]]

        val rule = ( compare: String ) ⇒ builder( _ == compare ) { value ⇒
            "expected" ->> value :: HNil
        }

        rule( "foo" ) shouldBe a[Rule[_, _, _]]
        rule( "foo" ).validate( "foo" ).isValid shouldBe true
        rule( "foo" ).validate( "" ).isValid shouldBe false
    }

    it should "allow to create simple rules with transformations" in {
        val builder = Rule[String, Int]( "min" )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _.length )( _ >= length )

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
        rule( 3 ).validate( "foobar" ).isValid shouldBe true
        rule( 3 ).validate( "" ).isValid shouldBe false
    }

    it should "allow to create simple rules with transformations and validation arguments" in {
        val builder = Rule[String, Int]( "min" )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _.length )( _ >= length ) { ( value, actual ) ⇒
            "expected" ->> length :: HNil
        }

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
        rule( 3 ).validate( "foobar" ).isValid shouldBe true
        rule( 3 ).validate( "" ).isValid shouldBe false
    }

    "matches" should "verify that the validation value equals the expectation" in {
        generic.matches( "foobar" ).validate( "foobar" ).isValid shouldBe true
        generic.matches( "" ).validate( "" ).isValid shouldBe true
        generic.matches( "foobar" ).validate( "foo" ).isValid shouldBe false

        generic.matches( 5 ).validate( 5 ).isValid shouldBe true
        generic.matches( 5 ).validate( 42 ).isValid shouldBe false
    }

    "required" should "verify that a String is not empty" in {
        generic.required[String].validate( "foo" ).isValid shouldBe true
        generic.required[String].validate( "" ).isValid shouldBe false
    }

    it should "verify that an Option is not empty" in {
        generic.required[Option[String]].validate( Some( "foo" ) ).isValid shouldBe true
        generic.required[Option[String]].validate( None ).isValid shouldBe false
    }

    "eq" should "verify that the validation value equals the expectation" in {
        numeric.eq( 5 ).validate( 5 ).isValid shouldBe true
        numeric.eq( 5 ).validate( 42 ).isValid shouldBe false
    }

    "gt" should "verify that the validation value is greater than the expectation" in {
        numeric.gt( 5 ).validate( 42 ).isValid shouldBe true
        numeric.gt( 5 ).validate( 5 ).isValid shouldBe false
        numeric.gt( 5 ).validate( 4 ).isValid shouldBe false
    }

    "gte" should "verify that the validation value is greater than or equal to the expectation" in {
        numeric.gte( 5 ).validate( 42 ).isValid shouldBe true
        numeric.gte( 5 ).validate( 5 ).isValid shouldBe true
        numeric.gte( 5 ).validate( 4 ).isValid shouldBe false
    }

    "lt" should "verify that the validation value is lesser than the expectation" in {
        numeric.lt( 5 ).validate( 4 ).isValid shouldBe true
        numeric.lt( 5 ).validate( 5 ).isValid shouldBe false
        numeric.lt( 5 ).validate( 42 ).isValid shouldBe false
    }

    "lte" should "verify that the validation value is lesser than or equal to the expectation" in {
        numeric.lte( 5 ).validate( 4 ).isValid shouldBe true
        numeric.lte( 5 ).validate( 5 ).isValid shouldBe true
        numeric.lte( 5 ).validate( 42 ).isValid shouldBe false
    }

    "email" should "verify that the validation value is an email address" in {
        string.email.validate( "mail@example.org" ).isValid shouldBe true
        string.email.validate( "" ).isValid shouldBe false
        string.email.validate( "foobar" ).isValid shouldBe false
        string.email.validate( "foobar@" ).isValid shouldBe false
        string.email.validate( "foobar@example" ).isValid shouldBe false
    }

    "exactly" should "verify that the validation value has the expected length" in {
        string.exactly( 6 ).validate( "foobar" ).isValid shouldBe true
        string.exactly( 0 ).validate( "" ).isValid shouldBe true
        string.exactly( 6 ).validate( "foo" ).isValid shouldBe false
        string.exactly( 6 ).validate( "" ).isValid shouldBe false
    }

    "max" should "verify that the validation value is shorter than the expected length" in {
        string.max( 6 ).validate( "foo" ).isValid shouldBe true
        string.max( 6 ).validate( "foobar" ).isValid shouldBe true
        string.max( 0 ).validate( "" ).isValid shouldBe true
        string.max( 6 ).validate( "foobar42" ).isValid shouldBe false
    }

    "min" should "verify that the validation value is longer than the expected length" in {
        string.min( 6 ).validate( "foobar42" ).isValid shouldBe true
        string.min( 6 ).validate( "foobar" ).isValid shouldBe true
        string.min( 0 ).validate( "" ).isValid shouldBe true
        string.min( 6 ).validate( "foo" ).isValid shouldBe false
    }

    "phone" should "verify that the validation value is a phone number" in {
        string.phone.validate( "123456" ).isValid shouldBe true
        string.phone.validate( "+49123456" ).isValid shouldBe true
        string.phone.validate( "" ).isValid shouldBe false
        string.phone.validate( "42foobar" ).isValid shouldBe false
        string.phone.validate( "foobar42" ).isValid shouldBe false
        string.phone.validate( "foobar42example" ).isValid shouldBe false
    }
}