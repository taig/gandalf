package io.taig.gandalf.report.ops

import io.taig.gandalf.report.{ Report, ReportableTerm }
import io.taig.gandalf.{ Error, Term }
import shapeless.{ HList, Witness }

class term[N <: String, I, O, A <: HList]( term: Term.Aux[N, I, O, A, Error[N, A]] )(
        implicit
        w: Witness.Aux[N]
) {
    def as[P]( message: A ⇒ P ): ReportableTerm[N, I, O, A, P] = ReportableTerm( term, Report.instance( message ) )

    def as0[P]( message: P ): ReportableTerm[N, I, O, A, P] = as( _ ⇒ message )
}