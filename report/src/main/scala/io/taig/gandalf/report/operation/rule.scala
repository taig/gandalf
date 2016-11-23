package io.taig.gandalf.report.operation

import cats.data.NonEmptyList
import io.taig.gandalf.report.Report

final class rule[V, I, O]( val value: V ) extends AnyVal {
    /**
     * Generate the Report of the Rule
     */
    def report( input: I )(
        implicit
        r: Report[V, I]
    ): NonEmptyList[String] = r.show( input )
}