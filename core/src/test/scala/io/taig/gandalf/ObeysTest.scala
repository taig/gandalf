package io.taig.gandalf

class ObeysTest extends Suite {
    it should "forward toString to the underlying value" in {
        Obeys.applyUnsafe[condition.success, String, String]( "foo" ).toString shouldBe
            "foo"
    }

    it should "convert implicitly unwrap" in {
        val value: String = Obeys.applyUnsafe[condition.success, String, String]( "foo" )
        value shouldBe "foo"
    }

    it should "provide unapply" in {
        val Obeys( value ) = Obeys.applyUnsafe[condition.success, String, String]( "foo" )
        value shouldBe "foo"
    }
}