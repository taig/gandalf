package io.taig.bsts.report.syntax

import cats.data.Validated
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

    implicit def reportableTermValidatedReportSyntax[N <: String, O, A <: HList](
        validated: Validated[ReportableError[N, A], O]
    ): ops.report[Validated[ReportableError[N, A], O]] = new ops.report( validated )

    implicit def termValidatedReportSyntax[N <: String, O, A <: HList](
        validated: Validated[Error[N, A], O]
    ): ops.report[Validated[Error[N, A], O]] = new ops.report( validated )

    implicit def termAsSyntax[N <: String, I, O, A <: HList](
        term: Term.Aux[N, I, O, A, Error[N, A]]
    )(
        implicit
        w: Witness.Aux[N]
    ): ops.term[N, I, O, A] = new ops.term( term )

    implicit def policyValidatedReportSyntax[C <: HList, O](
        validated: Validated[C, O]
    ): ops.report[Validated[C, O]] = new ops.report( validated )
}

object report extends report