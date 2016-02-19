package io.taig.bsts.report.syntax

import cats.data.Validated
import cats.data.NonEmptyList
import io.taig.bsts.ops

import scala.language.implicitConversions

trait cartesian {
    implicit def termValidatedToPolicyReport[O](
        validated: Validated[String, O]
    ): Validated[NonEmptyList[String], O] = validated.leftMap( NonEmptyList( _ ) )

    implicit def termValidatedReportCartesianSyntax[O](
        validated: Validated[String, O]
    ): ops.cartesian[String, O] = new ops.cartesian[String, O]( validated )
}

object cartesian extends cartesian