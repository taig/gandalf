package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait mutation {
    implicit def mutationSyntax[I, O, M <: Mutation.Aux[I, O]](
        mutation: M with Mutation.Aux[I, O]
    ): ops.mutation[I, O, M] = {
        new ops.mutation[I, O, M]( mutation )
    }
}

object mutation extends mutation