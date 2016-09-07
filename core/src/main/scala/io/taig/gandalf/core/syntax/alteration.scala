package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait alteration {
    implicit def gandalfCoreAlterationSyntax[A <: Alteration](
        alteration: A
    ): operation.alteration[A] = new operation.alteration[A]( alteration )
}

object alteration extends alteration