package io.taig.gandalf.core.syntax

import io.taig.gandalf.core._

import scala.language.implicitConversions

trait serialization {
    implicit def gandalfCoreSerializationSyntax[R <: Rule](
        rule: R
    ): operation.serialization[R] = new operation.serialization[R]( rule )
}

object serialization extends serialization