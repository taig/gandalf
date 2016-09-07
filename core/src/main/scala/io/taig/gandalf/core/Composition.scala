package io.taig.gandalf.core

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

final class Composition( rule: Rule ) extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro Composition.apply
}

object Composition {
    def apply( c: blackbox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        import c.universe._

        val tree = annottees.head.tree

        val q"new Composition( $rule )" = c.prefix.tree
        val typechecked = c.typecheck {
            q"""
            import io.taig.gandalf.syntax.all._
            $rule
            """
        }

        val ClassDef( mods, name, tparams, Template( parents, self, body ) ) = tree

        c.Expr {
            ClassDef(
                mods,
                name,
                tparams,
                Template( List( tq"${typechecked.tpe}" ), self, body )
            )
        }
    }
}