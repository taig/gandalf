package io.taig.bsts.tests

import io.taig.bsts.Policy
import io.taig.bsts.syntax.dsl._
import shapeless._

class PolicyTest extends Suite {
    "&" should "imitate a logical AND" in {
        ( rule.required & rule.min( 3 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required & rule.min( 6 ) ).validate( "foo" ).isValid shouldBe false
        ( rule.required & rule.max( 3 ) ).validate( "" ).isValid shouldBe false
        ( rule.required & rule.min( 6 ) ).validate( "" ).isValid shouldBe false
    }

    it should "always evaluate the rhs" in {
        intercept[IllegalStateException] {
            ( rule.required & rule.blow ).validate( "foo" )
        }

        intercept[IllegalStateException] {
            ( rule.required & rule.blow ).validate( "" )
        }
    }

    "&&" should "imitate a logical AND" in {
        ( rule.required && rule.min( 3 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required && rule.min( 6 ) ).validate( "foo" ).isValid shouldBe false
        ( rule.required && rule.max( 3 ) ).validate( "" ).isValid shouldBe false
        ( rule.required && rule.min( 6 ) ).validate( "" ).isValid shouldBe false
    }

    it should "only evaluate the rhs when lhs succeeds" in {
        ( rule.required && rule.blow ).validate( "" ).isValid shouldBe false

        intercept[IllegalStateException] {
            ( rule.required && rule.blow ).validate( "foo" )
        }
    }

    "|" should "imitate a logical OR" in {
        ( rule.required | rule.min( 3 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required | rule.min( 6 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required | rule.max( 3 ) ).validate( "" ).isValid shouldBe true
        ( rule.required | rule.min( 6 ) ).validate( "" ).isValid shouldBe false
    }

    it should "always evaluate the rhs" in {
        intercept[IllegalStateException] {
            ( rule.required | rule.blow ).validate( "foo" )
        }

        intercept[IllegalStateException] {
            ( rule.required | rule.blow ).validate( "" )
        }
    }

    "||" should "imitate a logical OR" in {
        ( rule.required || rule.min( 3 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required || rule.min( 6 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required || rule.max( 3 ) ).validate( "" ).isValid shouldBe true
        ( rule.required || rule.min( 6 ) ).validate( "" ).isValid shouldBe false
    }

    it should "only evaluate the rhs when lhs fails" in {
        ( rule.required || rule.blow ).validate( "foo" ).isValid shouldBe true

        intercept[IllegalStateException] {
            ( rule.required || rule.blow ).validate( "" )
        }
    }

    "^" should "imitate a logical XOR" in {
        ( rule.required ^ rule.min( 3 ) ).validate( "foo" ).isValid shouldBe false
        ( rule.required ^ rule.min( 6 ) ).validate( "foo" ).isValid shouldBe true
        ( rule.required ^ rule.max( 3 ) ).validate( "" ).isValid shouldBe true
        ( rule.required ^ rule.min( 6 ) ).validate( "" ).isValid shouldBe false
    }

    it should "always evaluate the rhs" in {
        intercept[IllegalStateException] {
            ( rule.required ^ rule.blow ).validate( "foo" )
        }

        intercept[IllegalStateException] {
            ( rule.required ^ rule.blow ).validate( "" )
        }
    }
}