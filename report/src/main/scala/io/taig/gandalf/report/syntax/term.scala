package io.taig.gandalf.report.syntax

import io.taig.gandalf.{ Error, Term }
import io.taig.gandalf.report.ops
import shapeless.{ HList, Witness }

import scala.language.implicitConversions

trait term {
    implicit def termSyntax[N <: String, I, O, A <: HList, E]( term: Term.Aux[N, I, O, A, Error[N, A]] )(
        implicit
        w: Witness.Aux[N]
    ): ops.term[N, I, O, A] = new ops.term[N, I, O, A]( term )
}

object term extends term