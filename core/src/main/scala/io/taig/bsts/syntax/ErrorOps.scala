package io.taig.bsts.syntax

import io.taig.bsts._
import shapeless.HList

final class ErrorOps[I <: String, A <: HList]( error: Error[I, A] ) {
    def report( implicit r: Report[I, A] ): String = r.report( error )
}