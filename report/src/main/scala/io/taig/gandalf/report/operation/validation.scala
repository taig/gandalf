package io.taig.gandalf.report.operation

import cats.data.{ Validated, ValidatedNel }
import io.taig.gandalf._
import io.taig.gandalf.report.Report

final class validation[I]( val input: I ) extends AnyVal {
    def validate[R <: Rule, O]( rule: R )(
        implicit
        v: Validation[R, I, O],
        r: Report[R, I, O]
    ): ValidatedNel[String, O] =
        Validated.fromOption( v.confirm( input ), r.show( input ) )
}