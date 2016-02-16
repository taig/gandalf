package io.taig.bsts.report

import io.taig.bsts.data.Validated
import io.taig.bsts.{ Error, Term }
import shapeless._

final case class ReportableTerm[N <: String, I, O, A <: HList](
        term:   Term.Aux[N, I, O, A, Validated[Error[N, A], O]],
        report: Report.Aux[Error[N, A], String]
)(
        implicit
        w: Witness.Aux[N]
) extends Term[N, I, O, A] {
    override type R = Validated[ReportableError[N, A], O]

    override type V = ReportableTerm[N, I, O, A] :: HNil

    override def validations = this :: HNil

    override def validate( input: I ) = term.validate( input ).leftMap( ReportableError( _, report ) )

    override def toString = "@" + super.toString
}