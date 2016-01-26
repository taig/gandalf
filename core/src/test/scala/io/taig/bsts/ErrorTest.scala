package io.taig.bsts

class ErrorTest extends Suite {
    "raw" should "provide an untyped result" in {
        rule.required.validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case Failure( error ) ⇒ error.raw shouldBe ( "required", Nil )
        }
    }
}