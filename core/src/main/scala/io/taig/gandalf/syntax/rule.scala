package io.taig.gandalf.syntax

import io.taig.gandalf._
import io.taig.gandalf.data.Rule

import scala.language.implicitConversions

trait rule {
    implicit def ruleSyntax[T, R <: Rule.Aux[T]](
        rule: R with Rule.Aux[T]
    ): operation.rule[T, R] = new operation.rule[T, R]( rule )
}

object rule extends rule