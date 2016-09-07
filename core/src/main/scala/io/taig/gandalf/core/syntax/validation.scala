package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait validation {
    implicit def gandalfCoreRuleSyntax[R <: Rule](
        rule: R
    ): operation.validation[R] = new operation.validation[R]( rule )
}

object validation extends validation