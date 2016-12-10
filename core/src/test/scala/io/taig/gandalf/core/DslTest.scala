package io.taig.gandalf.core

import io.taig.gandalf.core.syntax.dsl._
import io.taig.gandalf.core.syntax.rule._

class DslTest extends Suite {
    it should "support &&" in {
        ( condition.success && condition.success ).serialize shouldBe
            "(success && success)"
    }

    it should "support ||" in {
        ( condition.success || condition.success ).serialize shouldBe
            "(success || success)"
    }
}