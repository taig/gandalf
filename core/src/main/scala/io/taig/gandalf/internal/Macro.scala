package io.taig.gandalf.internal

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.gandalf.{ Error, Evaluation, Obeys, Validation }

import scala.reflect.macros.whitebox

object Macro {
    def lift[I <: V#Input, V <: Validation](
        c: whitebox.Context
    )(
        value: c.Expr[I]
    )(
        ev: c.Expr[Evaluation[V]],
        er: c.Expr[Error[V]]
    )(
        implicit
        i: c.WeakTypeTag[I],
        v: c.WeakTypeTag[V]
    ): c.Expr[I Obeys V] = {
        import c.universe._

        val validation = reify( ev.splice.validate( value.splice )( er.splice ) )
        val expression = c.Expr[Validated[List[String], V#Output]]( c.untypecheck( validation.tree ) )

        c.eval( expression ) match {
            case Valid( value ) ⇒
                c.Expr[I Obeys V](
                    q"""io.taig.gandalf.Obeys[$i, $v](
                        $expression.getOrElse {
                            throw new IllegalStateException(
                                "Runtime-validation failed. What the heck are you doing?!"
                            )
                        }
                    )"""
                )
            case Invalid( errors ) ⇒
                val messages = errors.mkString( "\n - ", "\n - ", "" )

                c.abort(
                    c.enclosingPosition,
                    s"Can not lift value '${show( value.tree )}' into ${v.tpe}:$messages"
                )
        }
    }

    def obeys( c: whitebox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        import c.universe._
        import termNames.CONSTRUCTOR

        val trees = annottees.map( _.tree )

        val target = c.prefix.tree match {
            case q"new obeys[$validation]" ⇒ validation
            case q"new obeys( $validation )" ⇒
                c.typecheck {
                    q"""
                    import io.taig.gandalf.syntax.all._
                    $validation
                    """
                }
        }

        def newType( lhs: Tree ) = tq"io.taig.gandalf.Obeys[$lhs, $target]"

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
}
