package io.taig.gandalf.typelevel.syntax

import io.taig.gandalf.typelevel._

import scala.language.implicitConversions

trait rule {
    implicit def ruleSyntax[R <: Rule]( rule: Evaluation[R] ): ops.rule[R] = new ops.rule[R]( rule )
}

object rule extends rule