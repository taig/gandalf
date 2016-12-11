package io.taig.gandalf.macros

import io.taig.gandalf.Rule

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

final class rule( composition: Rule ) extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro rule.apply
}

private object rule {
    def apply( c: blackbox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        import c.universe._

        val q"new rule( $rule )" = c.prefix.tree
        val typechecked = c.typecheck( q"$rule" )

        val tree = annottees.head.tree
        val ClassDef( mods, name, tparams, Template( _, self, body ) ) = tree

        c.Expr {
            q"""
            ${
                ClassDef(
                    mods,
                    name,
                    tparams,
                    Template( List( tq"${typechecked.tpe}" ), self, body )
                )
            }
            
            object ${name.toTermName} extends $name
            """
        }
    }
}