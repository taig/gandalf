package io.taig.gandalf.typelevel.annotation

import io.taig.gandalf.typelevel.Macro._

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros

@compileTimeOnly( "Must be used with macro paradise" )
class Rule[I] extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro rule_impl[I]
}