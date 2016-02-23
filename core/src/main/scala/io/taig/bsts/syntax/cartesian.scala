package io.taig.bsts.syntax

import cats.data._
import io.taig.bsts._
import io.taig.bsts.syntax.raw._
import shapeless.HList

import scala.language.implicitConversions

trait cartesian {
    implicit def validatedErrorToRawValidated[N <: String, O, A <: HList]( validated: Validated[Error[N, A], O] )(
        implicit
        r: Raw[Error[N, A]]
    ): Validated[NonEmptyList[( String, List[Any] )], O] = validated.raw

    //    implicit def termRawValidatedCartesianSyntax[O](
    //        validated: Validated[( String, List[Any] ), O]
    //    ): ops.cartesian[( String, List[Any] ), O] = new ops.cartesian( validated )

    implicit def validatedErrorCartesianSyntax[N <: String, O, A <: HList]( validated: Validated[Error[N, A], O] )(
        implicit
        r: Raw[Error[N, A]]
    ): ops.cartesian[O] = new ops.cartesian( validated.raw )

    implicit def validatedComputationCartesianSyntax[C <: HList, O]( validated: Validated[C, O] )(
        implicit
        r: Raw[C]
    ): ops.cartesian[O] = new ops.cartesian( validated.raw )
}

object cartesian extends cartesian