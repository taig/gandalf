package io.taig.gandalf.report.syntax

import io.taig.gandalf.report.operation

import scala.language.implicitConversions

trait validation {
    implicit def gandalfReportValidation[I]( input: I ): operation.validation[I] =
        new operation.validation[I]( input )
}

object validation extends validation