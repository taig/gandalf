package io.taig.bsts.report.syntax

import cats.data.Validated
import io.taig.bsts.Error
import io.taig.bsts.report._
import shapeless.HList

import scala.language.implicitConversions

trait report {
    implicit def reportErrorSyntax[N <: String, A <: HList]( error: Error[N, A] ): ops.reportError[N, A] = {
        new ops.reportError[N, A]( error )
    }

    implicit def reportReportableErrorSyntax[N <: String, A <: HList, O](
        error: ReportableError[N, A, O]
    ): ops.reportReportableError[N, A, O] = {
        new ops.reportReportableError[N, A, O]( error )
    }

    implicit def reportValidatedSyntax[E, A]( validated: Validated[E, A] ): ops.reportValidated[E, A] = {
        new ops.reportValidated[E, A]( validated )
    }
}

object report extends report