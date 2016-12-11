package io.taig.gandalf.syntax

import io.taig.gandalf._

import scala.language.implicitConversions

trait dsl {
    implicit def gandalfCoreDsl[L <: Rule]( left: L ): operation.dsl[L] =
        new operation.dsl[L]( left )
}

object dsl extends dsl