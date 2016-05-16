package io.taig.gandalf.typelevel.annotation

import io.taig.gandalf.typelevel.Macro._
import io.taig.gandalf.typelevel.Validation

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros

@compileTimeOnly( "Must be used with macro paradise" )
class obeys[V <: Validation] extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro obeys_impl
}