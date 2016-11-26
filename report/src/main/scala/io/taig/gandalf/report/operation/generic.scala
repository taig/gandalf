package io.taig.gandalf.report.operation

import cats.data.{Validated, ValidatedNel}
import io.taig.gandalf.core._
import io.taig.gandalf.core.goaway.Validation
import io.taig.gandalf.report.Report

final class generic[I]( val input: I ) extends AnyVal {
    def validate[V, O]( input: I )(
        implicit
        v: Validation[V, I, O],
        r: Report[V, I]
    ): ValidatedNel[String, O] = {
        Validated.fromOption( v( input ), r.show( input ) )
    }
}