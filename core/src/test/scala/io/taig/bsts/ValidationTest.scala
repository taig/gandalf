package io.taig.bsts

import scala.util.Left

class ValidationTest extends Suite {
    it should "provide an isSuccess check" in {
        val v1 = rule.required.validate( "" )
        val v2 = rule.required.validate( "foo" )

        v1.isSuccess shouldBe false
        v2.isSuccess shouldBe true
    }

    it should "provide an isFailure check" in {
        val v1 = rule.required.validate( "" )
        val v2 = rule.required.validate( "foo" )

        v1.isFailure shouldBe true
        v2.isFailure shouldBe false
    }

    it should "should be convertible to Either" in {
        val v1 = rule.required.validate( "" )
        val v2 = rule.required.validate( "foo" )

        v1 match {
            case Success( _ )     ⇒ fail()
            case Failure( error ) ⇒ v1.toEither shouldBe Left( error )
        }

        v2 match {
            case Success( value ) ⇒ v2.toEither shouldBe Right( value )
            case Failure( _ )     ⇒ fail()
        }
    }

    it should "should have a useful toString representation" in {
        rule.required.validate( "" ).toString shouldBe "Failure(Error(required))"
        rule.required.validate( "foo" ).toString shouldBe "Success(foo)"
    }
}