package io.taig.gandalf

import io.taig.gandalf.{ not ⇒ dont }
import io.taig.gandalf.syntax.dsl._
import io.taig.gandalf.syntax.rule._

class DslTest extends Suite {
    it should "support &&" in {
        ( condition.success && condition.success ).serialize shouldBe
            "(success && success)"
    }

    it should "support ||" in {
        ( condition.success || condition.success ).serialize shouldBe
            "(success || success)"
    }

    it should "support not" in {
        dont( condition.success ).serialize shouldBe "not(success)"
    }
}