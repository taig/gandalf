package io.taig.gandalf.core.test

import io.taig.gandalf.core._
import io.taig.gandalf.core.syntax.all._

class SerializationTest extends Suite {
    it should "have a Rule extension method" in {
        Serialization[condition.success].serialize shouldBe
            condition.success.serialize
        Serialization[condition.failure].serialize shouldBe
            condition.failure.serialize
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