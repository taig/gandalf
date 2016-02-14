package io.taig.bsts.syntax

import io.taig.bsts.data.{ NonEmptyList, Validated }
import io.taig.bsts.ops.Computed
import io.taig.bsts._
import shapeless.HList

import scala.language.implicitConversions

trait raw {
    implicit def errorRawSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.raw[Error[N, A], ( String, List[Any] )] = new ops.raw( error )

    implicit def termValidatedRawSyntax[N <: String, T, A <: HList](
        validated: Validated[Error[N, A], T]
    ): ops.raw[Validated[Error[N, A], T], Validated[( String, List[Any] ), T]] = new ops.raw( validated )

    implicit def policyValidatedRawSyntax[C <: HList, O](
        validated: Validated[Computed[C], O]
    ): ops.raw[Validated[Computed[C], O], Validated[NonEmptyList[( String, List[Any] )], O]] = new ops.raw( validated )
}

object raw extends raw