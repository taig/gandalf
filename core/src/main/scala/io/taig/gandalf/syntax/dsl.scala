package io.taig.gandalf.syntax

import io.taig.gandalf.{ Validation, ops }
import shapeless._

import scala.language.implicitConversions

trait dsl {
    implicit def dslRuleSyntax[I, O, V <: HList, E](
        validation: Validation.Aux[I, O, V, E]
    ): ops.dsl.logic[I, O, V, E] = new ops.dsl.logic( validation )

    implicit def dslTransformationSyntax[I, O, V <: HList, E](
        validation: Validation.Aux[I, O, V, E]
    ): ops.dsl.transformation[I, O, V, E] = new ops.dsl.transformation( validation )
}

object dsl extends dsl