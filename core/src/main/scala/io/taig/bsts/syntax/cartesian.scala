package io.taig.bsts.syntax

import cats.data._
import io.taig.bsts._
import io.taig.bsts.syntax.raw._
import shapeless.HList

import scala.language.implicitConversions

trait cartesian {
    //    implicit def rawValidatedToPolicyRawValidated[E, O]( validated: Validated[E, O] ): Validated[NonEmptyList[E], O] = {
    //        validated.leftMap( NonEmptyList( _ ) )
    //    }
    //
    //    implicit def termValidatedToPolicyRawValidated[N <: String, O, A <: HList]( validated: Validated[Error[N, A], O] )(
    //        implicit
    //        r: Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]]
    //    ): Validated[NonEmptyList[( String, List[Any] )], O] = validated.raw
    //
    //    implicit def termRawValidatedCartesianSyntax[O](
    //        validated: Validated[( String, List[Any] ), O]
    //    ): ops.cartesian[( String, List[Any] ), O] = new ops.cartesian( validated )
    //
    //    implicit def termValidatedCartesianSyntax[N <: String, O, A <: HList]( validated: Validated[Error[N, A], O] )(
    //        implicit
    //        r: Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]]
    //    ): ops.cartesian[( String, List[Any] ), O] = new ops.cartesian( validated.raw )
    //
    //    implicit def policyValidatedCartesianSyntax[C <: HList, O]( validated: Validated[C, O] )(
    //        implicit
    //        r: Raw[Validated[C, O], Validated[NonEmptyList[( String, List[Any] )], O]]
    //    ): ops.cartesian[( String, List[Any] ), O] = new ops.cartesian( validated.raw )
}

object cartesian extends cartesian