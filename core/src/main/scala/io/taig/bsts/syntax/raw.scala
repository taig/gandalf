package io.taig.bsts.syntax

import io.taig.bsts._
import shapeless.HList

import scala.language.implicitConversions

trait raw {
    implicit def errorRawSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.raw[Error[N, A]] = new ops.raw( error )

    implicit def ruleFailureRawSyntax[N <: String, T, A <: HList](
        failure: Failure[Error[N, A], T]
    ): ops.raw[Failure[Error[N, A], T]] = new ops.raw( failure )

    implicit def policyFailureRawSyntax[C <: HList, O](
        failure: Failure[Computed[C], O]
    ): ops.raw[Failure[Computed[C], O]] = new ops.raw( failure )
}

object raw extends raw