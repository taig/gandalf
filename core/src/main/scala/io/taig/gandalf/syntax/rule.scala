package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait rule {
    implicit def ruleSyntax[T, R <: Rule.Aux[T]](
        rule: R with Rule.Aux[T]
    ): ops.rule[T, R] = new ops.rule[T, R]( rule )
}

object rule extends rule