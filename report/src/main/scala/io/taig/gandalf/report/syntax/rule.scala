package io.taig.gandalf.report.syntax

import io.taig.gandalf.Rule
import io.taig.gandalf.report._

import scala.language.implicitConversions

trait rule {
    implicit def gandalfReportRule[R <: Rule, I]( rule: R ): operation.rule[R, I] =
        new operation.rule[R, I]( rule )
}

object rule extends rule