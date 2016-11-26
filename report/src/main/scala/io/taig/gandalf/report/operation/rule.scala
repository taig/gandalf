package io.taig.gandalf.report.operation

import cats.data.NonEmptyList
import io.taig.gandalf.core._
import io.taig.gandalf.report.Report

final class rule[R <: Rule, I]( val rule: R ) extends AnyVal {
    def report( input: I )(
        implicit
        r: Report[R, I]
    ): NonEmptyList[String] = r.show( input )
}