package io.taig.bsts.syntax

import io.taig.bsts._
import shapeless.HList

import scala.language.implicitConversions

trait error {
    implicit def errorSyntax[I <: String, A <: HList]( error: Error[I, A] ): ops.report[Error[I, A]] = {
        new ops.report( error )
    }
}

object error extends error