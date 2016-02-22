package io.taig.bsts.report

import io.taig.bsts.{ Error, Term }
import shapeless._

case class ReportableTerm[N <: String, I, O, A <: HList, P](
        term:   Term.Aux[N, I, O, A, Error[N, A]],
        report: Report[Error[N, A], P]
)(
        implicit
        w: Witness.Aux[N]
) extends Term[N, I, O, A] {
    override type E = ReportableError[N, A, P]

    override type V = ReportableTerm[N, I, O, A, P] :: HNil

    override def validations = this :: HNil

    override def validate( input: I ) = term.validate( input ).leftMap( ReportableError( _, report ) )

    override def toString = "@" + super.toString
}