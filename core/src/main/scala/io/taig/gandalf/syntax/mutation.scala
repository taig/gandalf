package io.taig.gandalf.syntax

import io.taig.gandalf._
import io.taig.gandalf.data.Mutation

import scala.language.implicitConversions

trait mutation {
    implicit def mutationSyntax[I, O, M <: Mutation.Aux[I, O]](
        mutation: M with Mutation.Aux[I, O]
    ): operation.mutation[I, O, M] = {
        new operation.mutation[I, O, M]( mutation )
    }
}

object mutation extends mutation