package io.taig.gandalf.typelevel.syntax

import io.taig.gandalf.typelevel._

import scala.language.implicitConversions

trait mutation {
    implicit def mutationSyntax[M <: Mutation]( mutation: Evaluation[M] ): ops.mutation[M] = new ops.mutation[M]( mutation )
}

object mutation extends mutation