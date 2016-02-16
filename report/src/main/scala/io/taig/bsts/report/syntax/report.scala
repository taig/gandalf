package io.taig.bsts.report.syntax

import cats.data.Xor
import io.taig.bsts.ops.Computed
import io.taig.bsts.report._
import io.taig.bsts.{ Error, Term }
import shapeless.{ HList, Witness }

import scala.language.implicitConversions

trait report {
    implicit def errorReportSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.report[Error[N, A]] = new ops.report( error )

    implicit def reportableErrorReportSyntax[N <: String, A <: HList](
        error: ReportableError[N, A]
    ): ops.report[ReportableError[N, A]] = new ops.report( error )

    implicit def reportableTermXorReportSyntax[N <: String, O, A <: HList](
        validated: Xor[ReportableError[N, A], O]
    ): ops.report[Xor[ReportableError[N, A], O]] = new ops.report( validated )

    implicit def termXorReportSyntax[N <: String, O, A <: HList](
        validated: Xor[Error[N, A], O]
    ): ops.report[Xor[Error[N, A], O]] = new ops.report( validated )

    implicit def termAsSyntax[N <: String, I, O, A <: HList](
        term: Term.Aux[N, I, O, A, Xor[Error[N, A], O]]
    )(
        implicit
        w: Witness.Aux[N]
    ): ops.term[N, I, O, A] = new ops.term( term )

    implicit def policyXorReportSyntax[C <: HList, O](
        validated: Xor[Computed[C], O]
    ): ops.report[Xor[Computed[C], O]] = new ops.report( validated )
}

object report extends report