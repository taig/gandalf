package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait mutation {
    implicit def mutationSyntax[M <: Mutation](
        mutation: M
    ): operation.mutation[M] = operation.mutation[M]( mutation )
}

object mutation extends mutation