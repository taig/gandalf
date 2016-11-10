package io.taig.gandalf.macros

import io.taig.gandalf.core.Rule

import scala.meta._

class obeys2( rule: Rule ) extends scala.annotation.StaticAnnotation {
    inline def apply( defn: Any ): Any = meta {
        q"{}"
    }
}