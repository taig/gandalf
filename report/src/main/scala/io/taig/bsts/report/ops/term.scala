package io.taig.bsts.report.ops

import io.taig.bsts.data.Validated
import io.taig.bsts.report.{ Report, ReportableTerm }
import io.taig.bsts.{ Error, Term }
import shapeless.{ Witness, HList }

final class term[N <: String, I, O, A <: HList]( term: Term.Aux[N, I, O, A, Validated[Error[N, A], O]] )(
        implicit
        w: Witness.Aux[N]
) {
    def as[R]( message: A ⇒ String ): ReportableTerm[N, I, O, A] = ReportableTerm( term, Report.instance( message ) )
}