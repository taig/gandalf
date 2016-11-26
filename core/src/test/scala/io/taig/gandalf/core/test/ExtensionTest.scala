package io.taig.gandalf.core.test

import io.taig.gandalf.core.{ not ⇒ dont, _ }
import io.taig.gandalf.core.syntax.all._

class ExtensionTest extends Suite {
    "&&" should "support compose" in {
        object custom1 extends ( condition.success && condition.success )
        object custom2 extends ( condition.success && condition.failure )

        "foo".confirm( custom1 ) shouldBe Some( "foo" )
        "foo".confirm( custom2 ) shouldBe None
    }

    it should "support negating compose" in {
        object custom1 extends ( condition.success && condition.success )
        object custom2 extends ( condition.success && condition.failure )

        "foo".confirm( dont( custom1 ) ) shouldBe None
        "foo".confirm( dont( custom2 ) ) shouldBe Some( "foo" )
    }

    it should "support compose with nested negation" in {
        object custom1 extends ( dont[condition.success] && condition.success )
        object custom2 extends ( condition.success && dont[condition.failure] )
        object custom3 extends ( dont[condition.failure] && dont[condition.success] )

        "foo".confirm( custom1 ) shouldBe None
        "foo".confirm( custom2 ) shouldBe Some( "foo" )
        "foo".confirm( custom3 ) shouldBe None
    }

    "||" should "support compose" in {
        object custom1 extends ( condition.success || condition.success )
        object custom2 extends ( condition.failure || condition.failure )

        "foo".confirm( custom1 ) shouldBe Some( "foo" )
        "foo".confirm( custom2 ) shouldBe None
    }

    it should "support negating compose" in {
        object custom1 extends ( condition.success || condition.success )
        object custom2 extends ( condition.failure || condition.failure )

        "foo".confirm( dont( custom1 ) ) shouldBe None
        "foo".confirm( dont( custom2 ) ) shouldBe Some( "foo" )
    }

    it should "support compose with nested negation" in {
        object custom1 extends ( dont[condition.success] || condition.success )
        object custom2 extends ( condition.failure || dont[condition.success] )
        object custom3 extends ( dont[condition.success] || dont[condition.failure] )

        "foo".confirm( custom1 ) shouldBe Some( "foo" )
        "foo".confirm( custom2 ) shouldBe None
        "foo".confirm( custom3 ) shouldBe Some( "foo" )
    }
}