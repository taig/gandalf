package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait rule {
    implicit def ruleSyntax[R <: Rule]( rule: R ): operation.rule[R] = {
        new operation.rule[R]
    }
}

object rule extends rule