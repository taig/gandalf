package io.taig.gandalf.core.test

import cats.data.Validated._
import io.taig.gandalf.core._
import io.taig.gandalf.core.syntax.all._

class ExtensionTest extends Suite {
    it should "be possible to compose a Mutation with a Mutation" in {
        object custom extends ( mutation.success.type ~> mutation.failure.type )

        custom.validate( "foo" ) shouldBe invalidNel( "mutation" )
    }

    it should "be possible to compose a Mutation with a Condition" in {
        object custom extends ( mutation.success.type ~> condition.failure.type )

        custom.validate( "foo" ) shouldBe invalidNel( "condition" )
    }

    it should "be possible to compose Conditions with an EagerAnd" in {
        object custom extends ( condition.success.type & condition.failure.type )

        custom.validate( "foo" ) shouldBe invalidNel( "condition" )
    }

    it should "be possible to compose Conditions with a LazyAnd" in {
        object custom extends ( condition.success.type && condition.failure.type )

        custom.validate( "foo" ) shouldBe invalidNel( "condition" )
    }

    it should "be possible to compose Conditions with an Or" in {
        object custom extends ( condition.success.type || condition.failure.type )

        custom.validate( "foo" ) shouldBe valid( "foo" )
    }

    it should "be possible to create custom error messages for Mutate (~>) compositions" in {
        object custom extends ( mutation.success.type ~> condition.failure.type ) {
            implicit val error: Error[this.type] = Error.one( _ ⇒ "custom" )
        }

        custom.validate( "foo" ) shouldBe invalidNel( "custom" )
    }

    it should "be possible to create custom error messages for EagerAnd (&) compositions" in {
        object custom extends ( condition.success.type & condition.failure.type ) {
            implicit val error: Error[this.type] = Error.one( _ ⇒ "custom" )
        }

        custom.validate( "foo" ) shouldBe invalidNel( "custom" )
    }

    it should "be possible to create custom error messages for LazyAnd (&&) compositions" in {
        object custom extends ( condition.success.type && condition.failure.type ) {
            implicit val error: Error[this.type] = Error.one( _ ⇒ "custom" )
        }

        custom.validate( "foo" ) shouldBe invalidNel( "custom" )
    }

    it should "be possible to create custom error messages for Or (||) compositions" in {
        object custom extends ( condition.failure.type || condition.failure.type ) {
            implicit val error: Error[this.type] = Error.one( _ ⇒ "custom" )
        }

        custom.validate( "foo" ) shouldBe invalidNel( "custom" )
    }
}