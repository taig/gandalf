package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait container {
    implicit def gandalfCoreContainerSyntax[C <: Container](
        container: C
    ): operation.container[C] = new operation.container[C]( container )
}

object container extends container