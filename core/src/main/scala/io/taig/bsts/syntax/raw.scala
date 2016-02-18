package io.taig.bsts.syntax

import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts._
import shapeless.HList

import scala.language.implicitConversions

trait raw {
    implicit def errorRawSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.raw[Error[N, A], ( String, List[Any] )] = new ops.raw( error )

    implicit def termValidatedRawSyntax[N <: String, O, A <: HList](
        validated: Validated[Error[N, A], O]
    ): ops.raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]] = new ops.raw( validated )

    implicit def policyValidatedRawSyntax[C <: HList, O](
        validated: Validated[C, O]
    ): ops.raw[Validated[C, O], Validated[NonEmptyList[( String, List[Any] )], O]] = new ops.raw( validated )
}

object raw extends raw