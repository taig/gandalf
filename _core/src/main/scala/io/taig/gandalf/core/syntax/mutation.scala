package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait mutation {
    implicit def gandalfCoreMutationSyntax[M <: Mutation](
        alteration: M
    ): operation.container[M] = new operation.container[M]( alteration )
}

object mutation extends container$