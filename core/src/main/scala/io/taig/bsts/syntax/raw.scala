package io.taig.bsts.syntax

import io.taig.bsts._
import shapeless.HList

import scala.language.implicitConversions

trait raw {
    implicit def errorRawSyntax[I <: String, A <: HList]( error: Error[I, A] ): ops.raw[Error[I, A]] = {
        new ops.raw( error )
    }

    implicit def ruleFailureRawSyntax[I <: String, T, A <: HList]( failure: Failure[Error[I, A], T] ): ops.raw[Failure[Error[I, A], T]] = {
        new ops.raw( failure )
    }

    implicit def policyFailureRawSyntax[C <: HList, T]( failure: Failure[Computed[C], T] ): ops.raw[Failure[Computed[C], T]] = {
        new ops.raw( failure )
    }
}

object raw extends raw