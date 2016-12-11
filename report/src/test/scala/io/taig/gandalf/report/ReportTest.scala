package io.taig.gandalf.report

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.core._
import io.taig.gandalf.report.syntax.all._

class ReportTest extends Suite {
    it should "a Rule extension method" in {
        condition.failure.report( "" ) shouldBe NonEmptyList.of( "Fehlschlag" )
    }

    it should "allow to perform validation" in {
        "".validate( condition.failure ) shouldBe invalidNel( "Fehlschlag" )
    }
}