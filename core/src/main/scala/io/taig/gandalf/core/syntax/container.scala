package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait container {
    implicit def gandalfCoreRuleSyntax[R <: Rule](
        container: R
    ): operation.container[R] = new operation.container[R]( container )
}

object container extends container