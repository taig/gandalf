package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait mutation {
    implicit def mutationSyntax[M <: Mutation]( mutation: M ): operation.mutation[M] = {
        new operation.mutation[M]
    }
}

object mutation extends mutation