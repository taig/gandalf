package io.taig.bsts.report.syntax

import io.taig.bsts.data.Validated
import io.taig.bsts.ops.Computed
import io.taig.bsts.report._
import io.taig.bsts.Error
import shapeless.HList

import scala.language.implicitConversions

trait report {
    implicit def errorReportSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.report[Error[N, A]] = new ops.report( error )

    implicit def termValidatedReportSyntax[N <: String, T, A <: HList](
        validated: Validated[Error[N, A], T]
    ): ops.report[Validated[Error[N, A], T]] = new ops.report( validated )

    implicit def policyValidatedReportSyntax[C <: HList, O](
        validated: Validated[Computed[C], O]
    ): ops.report[Validated[Computed[C], O]] = new ops.report( validated )
}

object report extends report