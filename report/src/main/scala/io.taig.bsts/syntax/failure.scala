package io.taig.bsts.syntax

import io.taig.bsts._
import io.taig.bsts.ops.report
import shapeless.HList

import scala.language.implicitConversions

trait failure {
    implicit def ruleFailureSyntax[I <: String, T, A <: HList]( failure: Failure[Error[I, A], T] ): ops.report[Failure[Error[I, A], T]] = {
        new report[Failure[Error[I, A], T]]( failure )
    }

    implicit def policyFailureSyntax[C <: HList, T]( failure: Failure[Computed[C], T] ): ops.report[Failure[Computed[C], T]] = {
        new report[Failure[Computed[C], T]]( failure )
    }
}

object failure extends failure