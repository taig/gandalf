package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[A <: Action]( action: A ): ops.validation[A] = new ops.validation[A]( action )
}

object validation extends validation