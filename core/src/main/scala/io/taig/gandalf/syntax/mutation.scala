package io.taig.gandalf.syntax

import io.taig.gandalf.{ Evaluation, Mutation }
import io.taig.gandalf._

import scala.language.implicitConversions

trait mutation {
    implicit def mutationSyntax[M <: Mutation]( mutation: Evaluation[M] ): ops.mutation[M] = {
        new ops.mutation[M]( mutation )
    }
}

object mutation extends mutation