package io.taig.gandalf.macros

import io.taig.gandalf.core.Rule

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

class obeys[R <: Rule]( rule: R ) extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro obeys.apply
}

private object obeys {
    def apply( c: whitebox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        import c.universe._

        val trees = annottees.map( _.tree )

        val input = annottees.head.tree match {
            case valDef: ValOrDefDef ⇒ valDef.tpt
            case tree ⇒
                c.abort(
                    c.enclosingPosition,
                    s"@obeys can not be applied to '${show( tree )}'"
                )
        }

        val target = c.prefix.tree match {
            case q"new obeys[$validation]" ⇒ validation
            case q"new obeys( $validation )" ⇒
                val retyped = retype( c )( validation, input )
                c.typecheck( q"$retyped" )
            case _ ⇒ c.abort(
                c.enclosingPosition,
                "Illegal @obeys format. Can bei either @obeys[nonEmpty.type] " +
                    "or @obeys( trim ~> nonEmpty )"
            )
        }

        trees match {
            case List( tree ) ⇒ expandDef( c )( tree, target )
            case _            ⇒ expandClass( c )( trees, target )
        }
    }

    def expandClass( c: whitebox.Context )( trees: Seq[c.Tree], target: c.Tree ): c.Expr[Any] = {
        import c.universe._
        import termNames.CONSTRUCTOR

        val valDef = trees
            .collectFirst { case valDef: ValDef ⇒ valDef }
            .getOrElse {
                c.abort(
                    c.enclosingPosition,
                    "@obeys can only be applied to val fields"
                )
            }

        val ClassDef( mods, name, tparams, Template( parents, self, body ) ) = trees
            .collectFirst { case classDef: ClassDef ⇒ classDef }
            .getOrElse {
                c.abort(
                    c.enclosingPosition,
                    "@obeys can only be used for case class fields"
                )
            }

        val newBody = body.map {
            case t @ ValDef( mods, name, tpt, rhs ) if valDef equalsStructure t ⇒
                ValDef( mods, name, obey( c )( tpt, target ), rhs )
            case constructor @ DefDef( mods, CONSTRUCTOR, tparams, vparamss, tpt, rhs ) ⇒
                val newVparamss = vparamss.map { vparams ⇒
                    vparams.map {
                        case t @ ValDef( mods, name, tpt, rhs ) if valDef.name == t.name ⇒
                            ValDef( mods, name, obey( c )( tpt, target ), rhs )
                        case default ⇒ default
                    }
                }

                DefDef( mods, CONSTRUCTOR, tparams, newVparamss, tpt, rhs )
            case default ⇒ default
        }

        c.Expr( ClassDef( mods, name, tparams, Template( parents, self, newBody ) ) )
    }

    def expandDef( c: whitebox.Context )( tree: c.Tree, target: c.Tree ): c.Expr[Any] = {
        import c.universe._

        val valDef = tree match {
            case ValDef( mods, name, tpt, rhs ) ⇒
                ValDef( mods, name, obey( c )( tpt, target ), rhs )
            case DefDef( mods, name, tparams, vparams, tpt, rhs ) ⇒
                DefDef( mods, name, tparams, vparams, obey( c )( tpt, target ), rhs )
        }

        c.Expr( valDef )
    }

    def obey( c: whitebox.Context )( lhs: c.Tree, rhs: c.Tree ) = {
        import c.universe._
        tq"_root_.io.taig.gandalf.macros.Obey[$lhs, $rhs]"
    }

    /**
     * Find the first Validation in the tree and make sure that its input is inferred correctly
     */
    def retype( c: whitebox.Context )( tree: c.Tree, input: c.Tree ): c.Tree = {
        import c.universe._

        tree match {
            case q"$left ~> $right" ⇒ q"${retype( c )( left, input )} ~> $right"
            case q"$left && $right" ⇒ q"${retype( c )( left, input )} && $right"
            case q"$left & $right"  ⇒ q"${retype( c )( left, input )} & $right"
            case q"$left || $right" ⇒ q"${retype( c )( left, input )} || $right"
            case rule ⇒
                q"""
                new _root_.io.taig.gandalf.macros.InferenceHelper[$input]
                    .infer( $rule )
                """
        }
    }
}