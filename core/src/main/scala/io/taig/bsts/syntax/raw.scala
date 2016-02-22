package io.taig.bsts.syntax

import cats.data.Validated
import io.taig.bsts._
import shapeless.HList

import scala.language.implicitConversions

trait raw {
    implicit def rawErrorSyntax[N <: String, A <: HList]( error: Error[N, A] )(
        implicit
        r: Raw[Error[N, A]]
    ): ops.rawError[N, A] = new ops.rawError[N, A]( error )

    implicit def rawValidatedSyntax[E, A]( validated: Validated[E, A] )(
        implicit
        r: Raw[E]
    ): ops.rawValidated[E, A] = new ops.rawValidated[E, A]( validated )
}

object raw extends raw