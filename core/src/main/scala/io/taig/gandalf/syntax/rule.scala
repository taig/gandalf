package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait rule {
    implicit def gandalfCoreRule[R <: Rule]( rule: R ): operation.rule[R] =
        new operation.rule[R]( rule )
}

object rule extends rule