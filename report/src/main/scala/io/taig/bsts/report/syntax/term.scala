package io.taig.bsts.report.syntax

import io.taig.bsts.{ Error, Term }
import io.taig.bsts.report.ops
import shapeless.{ HList, Witness }

import scala.language.implicitConversions

trait term {
    implicit def termSyntax[N <: String, I, O, A <: HList, E]( term: Term.Aux[N, I, O, A, Error[N, A]] )(
        implicit
        w: Witness.Aux[N]
    ): ops.term[N, I, O, A] = new ops.term[N, I, O, A]( term )
}

object term extends term