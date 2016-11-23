package io.taig.gandalf.core.test

import io.taig.gandalf.core._
import io.taig.gandalf.core.syntax.validation._

class ExtensionTest extends Suite {
    it should "be possible to compose Conditions with a And" in {
        object custom extends ( condition.success && condition.failure )

        "foo".confirm( custom ) shouldBe None
    }

    it should "be possible to compose Conditions with an Or" in {
        object custom extends ( condition.success || condition.failure )

        "foo".confirm( custom ) shouldBe Some( "foo" )
    }
}