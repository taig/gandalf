package io.taig.bsts.report.ops

import io.taig.bsts.report.{ Report, ReportableTerm }
import io.taig.bsts.{ Error, Term }
import shapeless.{ HList, Witness }

class term[N <: String, I, O, A <: HList]( term: Term.Aux[N, I, O, A, Error[N, A]] )(
        implicit
        w: Witness.Aux[N]
) {
    def as( message: A ⇒ String ): ReportableTerm[N, I, O, A] = ReportableTerm( term, Report.instance( message ) )

    def as( message: String ): ReportableTerm[N, I, O, A] = as( _ ⇒ message )
}