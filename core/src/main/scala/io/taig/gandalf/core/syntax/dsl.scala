package io.taig.gandalf.core.syntax

import io.taig.gandalf.core.{ Rule, operation }

import scala.language.implicitConversions

trait dsl {
    implicit def gandalfCoreDsl[L <: Rule]( left: L ): operation.dsl[L] =
        new operation.dsl[L]( left )
}

object dsl extends dsl