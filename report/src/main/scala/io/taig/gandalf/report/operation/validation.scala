package io.taig.gandalf.report.operation

import cats.data.{ Validated, ValidatedNel }
import io.taig.gandalf.core._
import io.taig.gandalf.report.Report

final class validation[I]( val input: I ) extends AnyVal {
    def validate[R <: Rule, O]( input: I )(
        implicit
        v: Validation[R, I, O],
        r: Report[R, I]
    ): ValidatedNel[String, O] = {
        Validated.fromOption( v( input ), r.show( input ) )
    }
}