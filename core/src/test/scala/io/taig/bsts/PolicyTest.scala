package io.taig.bsts

import shapeless._
import io.taig.bsts.syntax.dsl._

class PolicyTest extends Suite {
    it should "have a toString representation" in {
        Policy.Rule( rule.required :: HNil ).toString shouldBe "Policy.Rule(required)"
        Policy.Transformation( transformation.trim :: HNil ).toString shouldBe "Policy.Transformation(trim)"

        ( rule.required & rule.min( 3 ) ).toString shouldBe "Policy.Rule(required & min)"
        ( rule.required & rule.min( 3 ) && rule.max( 6 ) ).toString shouldBe "Policy.Rule((required & min) && max)"
        ( rule.required & ( rule.min( 3 ) && rule.max( 6 ) ) ).toString shouldBe "Policy.Rule(required & (min && max))"
        ( ( rule.required || rule.min( 3 ) ) & ( rule.min( 3 ) && rule.max( 6 ) ) ).toString shouldBe
            "Policy.Rule((required || min) & (min && max))"
    }

    "apply" should "allow to create a Policy from a single Rule" in {
        Policy( rule.required ) shouldBe Policy.Rule( rule.required :: HNil )
    }

    it should "allow to create a Policy from a single Transformation" in {
        Policy( transformation.trim ) shouldBe Policy.Transformation( transformation.trim :: HNil )
    }

    "&" should "imitate a logical AND" in {
        ( rule.required & rule.min( 3 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required & rule.min( 6 ) ).validate( "foo" ).isSuccess shouldBe false
        ( rule.required & rule.max( 3 ) ).validate( "" ).isSuccess shouldBe false
        ( rule.required & rule.min( 6 ) ).validate( "" ).isSuccess shouldBe false
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
        ( rule.required && rule.min( 3 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required && rule.min( 6 ) ).validate( "foo" ).isSuccess shouldBe false
        ( rule.required && rule.max( 3 ) ).validate( "" ).isSuccess shouldBe false
        ( rule.required && rule.min( 6 ) ).validate( "" ).isSuccess shouldBe false
    }

    it should "only evaluate the rhs when lhs succeeds" in {
        ( rule.required && rule.blow ).validate( "" ).isSuccess shouldBe false

        intercept[IllegalStateException] {
            ( rule.required && rule.blow ).validate( "foo" )
        }
    }

    "|" should "imitate a logical OR" in {
        ( rule.required | rule.min( 3 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required | rule.min( 6 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required | rule.max( 3 ) ).validate( "" ).isSuccess shouldBe true
        ( rule.required | rule.min( 6 ) ).validate( "" ).isSuccess shouldBe false
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
        ( rule.required || rule.min( 3 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required || rule.min( 6 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required || rule.max( 3 ) ).validate( "" ).isSuccess shouldBe true
        ( rule.required || rule.min( 6 ) ).validate( "" ).isSuccess shouldBe false
    }

    it should "only evaluate the rhs when lhs fails" in {
        ( rule.required || rule.blow ).validate( "foo" ).isSuccess shouldBe true

        intercept[IllegalStateException] {
            ( rule.required || rule.blow ).validate( "" )
        }
    }

    "^" should "imitate a logical XOR" in {
        ( rule.required ^ rule.min( 3 ) ).validate( "foo" ).isSuccess shouldBe false
        ( rule.required ^ rule.min( 6 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required ^ rule.max( 3 ) ).validate( "" ).isSuccess shouldBe true
        ( rule.required ^ rule.min( 6 ) ).validate( "" ).isSuccess shouldBe false
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