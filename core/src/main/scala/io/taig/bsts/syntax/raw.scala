package io.taig.bsts.syntax

import cats.data.{ NonEmptyList, Xor }
import io.taig.bsts._
import io.taig.bsts.ops.Computed
import shapeless.HList

import scala.language.implicitConversions

trait raw {
    implicit def errorRawSyntax[N <: String, A <: HList](
        error: Error[N, A]
    ): ops.raw[Error[N, A], ( String, List[Any] )] = new ops.raw( error )

    implicit def termXorRawSyntax[N <: String, T, A <: HList](
        validated: Xor[Error[N, A], T]
    ): ops.raw[Xor[Error[N, A], T], Xor[( String, List[Any] ), T]] = new ops.raw( validated )

    implicit def policyXorRawSyntax[C <: HList, O](
        validated: Xor[Computed[C], O]
    ): ops.raw[Xor[Computed[C], O], Xor[NonEmptyList[( String, List[Any] )], O]] = new ops.raw( validated )
}

object raw extends raw