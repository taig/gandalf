package io.taig.bsts

import shapeless._

class PolicyTest extends Suite {
    it should "provide an implicit conversion from a Rule" in {
        ( rule.required: Policy[String, Rule[Witness.`"required"`.T, String, HNil] :: HNil] ) shouldBe
            Policy( rule.required )
    }

    "apply" should "allow to create a Policy from a single Rule" in {
        Policy( rule.required ) shouldBe Policy( rule.required :: HNil )
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
        ( rule.required || rule.min( 3 ) ).validate( "foo" ).isSuccess shouldBe false
        ( rule.required || rule.min( 6 ) ).validate( "foo" ).isSuccess shouldBe true
        ( rule.required || rule.max( 3 ) ).validate( "" ).isSuccess shouldBe true
        ( rule.required || rule.min( 6 ) ).validate( "" ).isSuccess shouldBe false
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