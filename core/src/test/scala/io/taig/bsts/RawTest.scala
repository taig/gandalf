package io.taig.bsts

import io.taig.bsts.syntax.all._

class RawTest extends Suite {
    it should "be available for Errors" in {
        rule.required.validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case Failure( error ) ⇒ error.raw shouldBe ( "required", Nil )
        }
    }

    it should "be available for Rule Failures" in {
        rule.required.validate( "" ) match {
            case Success( _ )         ⇒ fail()
            case f @ Failure( error ) ⇒ f.raw shouldBe error.raw
        }
    }

    it should "be available for Transformation Failures" in {
        transformation.parse.validate( "asdf" ) match {
            case Success( _ )         ⇒ fail()
            case f @ Failure( error ) ⇒ f.raw shouldBe error.raw
        }
    }

    it should "be available for Policy Failures" in {
        Policy( rule.required ).validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case f @ Failure( _ ) ⇒ f.raw shouldBe List( ( "required", Nil ) )
        }

        //        ( rule.required & rule.min( 6 ) ).validate( "foo" ) match {
        //            case Success( _ )     ⇒ fail()
        //            case f @ Failure( _ ) ⇒ f.raw shouldBe List( ( "min", List( "foo", 6, 3 ) ) )
        //        }
    }
}