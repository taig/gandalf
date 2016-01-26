package io.taig.bsts

class ErrorTest extends Suite {
    it should "have a useful toString representation" in {
        rule.required.validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case Failure( error ) ⇒ error.toString shouldBe "Error(required)"
        }
    }
}