package io.taig.bsts.report.syntax

import io.taig.bsts.{ Computed, Error, Failure }
import io.taig.bsts.report._
import shapeless.HList

import scala.language.implicitConversions

trait report {
    implicit def errorReportSyntax[I <: String, A <: HList]( error: Error[I, A] ): ops.report[Error[I, A]] = {
        new ops.report( error )
    }

    implicit def ruleFailureReportSyntax[I <: String, T, A <: HList]( failure: Failure[Error[I, A], T] ): ops.report[Failure[Error[I, A], T]] = {
        new ops.report[Failure[Error[I, A], T]]( failure )
    }

    implicit def policyFailureReportSyntax[C <: HList, T]( failure: Failure[Computed[C], T] ): ops.report[Failure[Computed[C], T]] = {
        new ops.report[Failure[Computed[C], T]]( failure )
    }
}

object report extends report