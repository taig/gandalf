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
        import termNames.CONSTRUCTOR

        val trees = annottees.map( _.tree )
        val Expr( ValDef( _, _, input, _ ) ) = annottees.head

        val target = c.prefix.tree match {
            case q"new obeys[$validation]" ⇒
                validation
            case q"new obeys( $validation )" ⇒
                val retyped = retype( c )( validation, input )
                c.typecheck( q"$retyped" )
            case _ ⇒ c.abort(
                c.enclosingPosition,
                "Illegal @obeys format. Can bei either @obeys[trim.type ~> " +
                    "nonEmpty.type] or @obeys( trim ~> nonEmpty )"
            )
        }

        def newType( lhs: Tree ) = tq"_root_.io.taig.gandalf.macros.Obey[$lhs, $target]"

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
            case target @ ValDef( mods, name, tpt, rhs ) if valDef equalsStructure target ⇒
                ValDef( mods, name, newType( tpt ), rhs )
            case constructor @ DefDef( mods, CONSTRUCTOR, tparams, vparamss, tpt, rhs ) ⇒
                val newVparamss = vparamss.map { vparams ⇒
                    vparams.map {
                        case target @ ValDef( mods, name, tpt, rhs ) if valDef.name == target.name ⇒
                            ValDef( mods, name, newType( tpt ), rhs )
                        case default ⇒ default
                    }
                }

                DefDef( mods, CONSTRUCTOR, tparams, newVparamss, tpt, rhs )
            case default ⇒ default
        }

        c.Expr( ClassDef( mods, name, tparams, Template( parents, self, newBody ) ) )
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