package io.taig.gandalf.typelevel.syntax

import io.taig.gandalf.typelevel._

import scala.language.implicitConversions

trait RuleSyntax {
    implicit def ruleSyntax[R <: Rule]( rule: Evaluation[R] ): ops.RuleOps[R] = new ops.RuleOps[R]( rule )
}

object RuleSyntax extends RuleSyntax