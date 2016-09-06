package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait validation {
    implicit def gandalfCoreRuleSyntax[R <: Rule](
        rule: R
    ): operation.validation[R] = new operation.validation[R]( rule )
}

object validation extends validation