package io.taig.gandalf

import io.taig.gandalf.data.Action
import io.taig.gandalf.internal.Macro

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros

@compileTimeOnly( "Must be used with macro paradise" )
class obeys[A <: Action]( action: A ) extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro Macro.obeys
}