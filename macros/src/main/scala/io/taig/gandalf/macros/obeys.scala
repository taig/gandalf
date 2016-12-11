package io.taig.gandalf.macros

import io.taig.gandalf.core.Rule

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import scala.util.{ Success, Try }

class obeys[O]( rule: Rule ) extends StaticAnnotation {
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

        val ( rule, output ) = c.prefix.tree match {
            case q"new obeys[$output]( $validation )" ⇒
                ( c.typecheck( q"$validation" ), output )
            case q"new obeys( $validation )" ⇒
                ( c.typecheck( q"$validation" ), input )
            case _ ⇒ c.abort( c.enclosingPosition, "Illegal @obeys format" )
        }

        trees match {
            case List( tree ) ⇒ expandDef( c )( tree, rule, output )
            case _            ⇒ expandClass( c )( trees, rule, output )
        }
    }

    def expandClass( c: whitebox.Context )( trees: Seq[c.Tree], rule: c.Tree, output: c.Tree ): c.Expr[Any] = {
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
                ValDef( mods, name, obey( c )( rule, tpt, output ), rhs )
            case constructor @ DefDef( mods, CONSTRUCTOR, tparams, vparamss, tpt, rhs ) ⇒
                val newVparamss = vparamss.map { vparams ⇒
                    vparams.map {
                        case t @ ValDef( mods, name, tpt, rhs ) if valDef.name == t.name ⇒
                            ValDef( mods, name, obey( c )( rule, tpt, output ), rhs )
                        case default ⇒ default
                    }
                }

                DefDef( mods, CONSTRUCTOR, tparams, newVparamss, tpt, rhs )
            case default ⇒ default
        }

        c.Expr( ClassDef( mods, name, tparams, Template( parents, self, newBody ) ) )
    }

    def expandDef( c: whitebox.Context )( tree: c.Tree, rule: c.Tree, output: c.Tree ): c.Expr[Any] = {
        import c.universe._

        val valDef = tree match {
            case ValDef( mods, name, tpt, rhs ) ⇒
                ValDef( mods, name, obey( c )( rule, tpt, output ), rhs )
            case DefDef( mods, name, tparams, vparams, tpt, rhs ) ⇒
                DefDef( mods, name, tparams, vparams, obey( c )( rule, tpt, output ), rhs )
        }

        c.Expr( valDef )
    }

    def obey( c: whitebox.Context )( r: c.Tree, i: c.Tree, o: c.Tree ) = {
        import c.universe._
        tq"io.taig.gandalf.macros.Obey[$r, $i, $o]"
    }

    def tryN[T]( n: Int, t: ⇒ T ): T = {
        Stream
            .fill( n )( Try( t ) )
            .collectFirst { case Success( r ) ⇒ r }
            .getOrElse( t )
    }
}