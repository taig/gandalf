package io.taig.bsts.report.syntax

import io.taig.bsts.{ Computed, Error, Failure }
import io.taig.bsts.report._
import shapeless.HList

import scala.language.implicitConversions

trait report {
    implicit def errorReportSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.report[Error[N, A]] = new ops.report( error )

    implicit def ruleFailureReportSyntax[N <: String, T, A <: HList](
        failure: Failure[Error[N, A], T]
    ): ops.report[Failure[Error[N, A], T]] = new ops.report[Failure[Error[N, A], T]]( failure )

    implicit def policyFailureReportSyntax[C <: HList, T](
        failure: Failure[Computed[C], T]
    ): ops.report[Failure[Computed[C], T]] = new ops.report[Failure[Computed[C], T]]( failure )
}

object report extends report