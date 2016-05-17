package io.taig.gandalf.syntax

import io.taig.gandalf.{ Evaluation, Rule }
import io.taig.gandalf._

import scala.language.implicitConversions

trait rule {
    implicit def ruleSyntax[R <: Rule]( rule: Evaluation[R] ): ops.rule[R] = {
        new ops.rule[R]( rule )
    }
}

object rule extends rule