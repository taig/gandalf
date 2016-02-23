package io.taig.bsts.report

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.ops.NestedEvaluation
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

    override def validate( input: I ): Validated[ReportableError[N, A, P], O] = {
        term.validate( input ).leftMap( new ReportableError( _, report ) )
    }

    override def toString = "@" + super.toString
}

object ReportableTerm {
    implicit def nestedEvaluationReportableTerm[N <: String, I, O, A <: HList, P] = {
        new NestedEvaluation[I, O, ReportableTerm[N, I, O, A, P] :: HNil] {
            override type Out0 = Validated[ReportableError[N, A, P], O] :: HNil

            override def apply( input: I, tree: ReportableTerm[N, I, O, A, P] :: HNil ) = tree match {
                case reportableTerm :: HNil ⇒ reportableTerm.validate( input ) match {
                    case valid @ Valid( output ) ⇒ ( Some( output ), valid :: HNil )
                    case invalid @ Invalid( _ )  ⇒ ( None, invalid :: HNil )
                }
            }
        }
    }
}