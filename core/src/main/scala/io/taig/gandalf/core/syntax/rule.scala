package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait rule {
    implicit def gandalfCoreRule[R <: Rule]( rule: R ): operation.rule[R] =
        new operation.rule[R]( rule )
}

object rule extends rule