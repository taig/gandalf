package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait condition {
    implicit def gandalfCoreConditionSyntax[C <: Condition](
        condition: C
    ): operation.condition[C] = new operation.condition[C]( condition )
}

object condition extends condition