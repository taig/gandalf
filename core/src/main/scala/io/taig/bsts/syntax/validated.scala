package io.taig.bsts.syntax

import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts._
import io.taig.bsts.syntax.raw._
import shapeless.HList

import scala.language.implicitConversions

trait validated {
    implicit def validatedErrorSyntax[N <: String, O, A <: HList]( validated: Validated[Error[N, A], O] )(
        implicit
        r: Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]]
    ): ops.validated[N, O, A] = new ops.validated( validated )

    implicit def `Validated[Error] -> Validated[NonEmptyList[Raw]]`[N <: String, O, A <: HList](
        validated: Validated[Error[N, A], O]
    )(
        implicit
        r: Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]]
    ): Validated[NonEmptyList[( String, List[Any] )], O] = validated.raw.leftMap( NonEmptyList( _ ) )
}

object validated extends validated