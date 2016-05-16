package io.taig.gandalf.typelevel

import io.taig.gandalf.typelevel.Macro.obeys_impl

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros

case class Obey[L, R <: Validation]( value: R#Output ) extends AnyVal with Serializable {
    override def toString = value.toString
}

@compileTimeOnly( "Must be used with macro paradise" )
class obeys[V <: Validation] extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro obeys_impl
}