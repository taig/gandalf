package io.taig.gandalf

import io.taig.gandalf.internal.Macro

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros

@compileTimeOnly( "Must be used with macro paradise" )
class obeys[I]( validation: Action.Input[I] ) extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro Macro.obeys
}