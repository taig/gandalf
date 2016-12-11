package io.taig.gandalf

import io.taig.gandalf.syntax.all._

class SerializationTest extends Suite {
    it should "have a Rule extension method" in {
        Serialization[condition.success].serialize shouldBe
            condition.success.serialize
        Serialization[condition.failure].serialize shouldBe
            condition.failure.serialize
    }

    it should "have a an equals implementation" in {
        Serialization[condition.success] == Serialization[condition.success] shouldBe
            true
        Serialization[condition.success] == Serialization[condition.failure] shouldBe
            false
        Serialization[condition.success] == 0 shouldBe
            false
    }

    "Condition" should "have a String representation" in {
        Serialization[condition.success].serialize shouldBe "success"
        Serialization[condition.failure].serialize shouldBe "failure"
    }

    "Mutation" should "have a String representation" in {
        Serialization[mutation.success].serialize shouldBe "success"
        Serialization[mutation.failure].serialize shouldBe "failure"
    }

    "Transition" should "have a String representation" in {
        Serialization[transition.string].serialize shouldBe "string"
    }

    "&&" should "have a String representation" in {
        Serialization[condition.success && transition.string].serialize shouldBe
            "(success && string)"
    }

    "||" should "have a String representation" in {
        Serialization[condition.success || transition.string].serialize shouldBe
            "(success || string)"
    }

    "not" should "have a String representation" in {
        Serialization[not[condition.success]].serialize shouldBe "not(success)"
        Serialization[not[condition.failure]].serialize shouldBe "not(failure)"
    }
}