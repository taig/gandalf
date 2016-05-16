package io.taig.gandalf.typelevel

import cats.data.Validated.{ Invalid, Valid }
import cats.data.{ Validated, ValidatedNel }
import cats.implicits._

import scala.reflect.macros._

object Macro {
    def lift_impl[I <: V#Input, V <: Validation](
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

        val validation = reify( ev.splice.validate( value.splice ) )
        val expression = c.Expr[Validated[List[String], V#Output]]( c.untypecheck( validation.tree ) )

        c.eval( expression ) match {
            case Valid( value ) ⇒
                c.Expr[I Obeys V](
                    q"""io.taig.gandalf.typelevel.Obeys[$i, $v](
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

    def obeys_impl( c: whitebox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        import c.universe._
        import termNames.CONSTRUCTOR

        val trees = annottees.map( _.tree )

        val annotation = c.prefix
        val q"new obeys[$validation]" = annotation.tree
        def newType( lhs: Tree ) = tq"io.taig.gandalf.typelevel.Obeys[$lhs,$validation]"

        val valDef = trees
            .collectFirst { case valDef: ValDef ⇒ valDef }
            .getOrElse {
                c.abort(
                    c.enclosingPosition,
                    "@obeys can only be applied to val fields"
                )
            }

        val classDef = trees
            .collectFirst { case classDef: ClassDef ⇒ classDef }
            .getOrElse {
                c.abort(
                    c.enclosingPosition,
                    "@obeys can only be used for case class fields"
                )
            }

        val result = classDef match {
            case classDef @ ClassDef( mods, name, tparams, Template( parents, self, body ) ) ⇒
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

                ClassDef( mods, name, tparams, Template( parents, self, newBody ) )
        }

        c.Expr( result )
    }

    def rule_impl[I]( c: whitebox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = annottees.head
}