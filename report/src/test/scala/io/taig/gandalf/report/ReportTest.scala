package io.taig.gandalf.report

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf._
import io.taig.gandalf.{ not ⇒ dont }
import io.taig.gandalf.syntax.dsl._
import io.taig.gandalf.report.syntax.all._

class ReportTest extends Suite {
    it should "provide a Rule extension method" in {
        condition.failure.report( "" ) shouldBe NonEmptyList.of( "failure" )
    }

    it should "allow to perform validation" in {
        "".validate( condition.failure ) shouldBe invalidNel( "failure" )
    }

    it should "provide a default message for not" in {
        "".validate( dont( condition.success ) ) shouldBe
            invalidNel( "not(success)" )
    }

    it should "provide a default message for &&" in {
        "".validate( condition.success && condition.failure ) shouldBe
            invalid( NonEmptyList.of( "success", "failure" ) )
    }

    it should "provide a default message for ||" in {
        "".validate( condition.failure || condition.failure ) shouldBe
            invalid( NonEmptyList.of( "failure", "failure" ) )
    }
}