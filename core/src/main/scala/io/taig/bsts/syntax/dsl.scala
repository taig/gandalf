package io.taig.bsts.syntax

import io.taig.bsts.{ Validation, ops }
import shapeless._

import scala.language.implicitConversions

trait dsl {
    implicit def validationRuleSyntax[I, O, V <: HList, R](
        validation: Validation.Aux[I, O, V, R]
    ): ops.dsl.logic[I, O, V, R] = new ops.dsl.logic( validation )

    implicit def validationTransformationSyntax[I, O, V <: HList, R](
        validation: Validation.Aux[I, O, V, R]
    ): ops.dsl.transformation[I, O, V, R] = new ops.dsl.transformation( validation )
}

object dsl extends dsl