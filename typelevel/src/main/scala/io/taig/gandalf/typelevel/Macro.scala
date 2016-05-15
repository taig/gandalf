package io.taig.gandalf.typelevel

import cats.data.Validated.{ Invalid, Valid }
import cats.data.ValidatedNel
import cats.implicits._

import scala.reflect.macros._

object Macro {
    private val classLoader = getClass.getClassLoader

    def lift_impl[V <: Validation](
        c: whitebox.Context
    )(
        value: c.Expr[V#Input]
    )(
        e: c.Expr[Evaluation[V]]
    )(
        implicit
        v: c.WeakTypeTag[V]
    ): c.Expr[V#Input Obeys V] = {
        import c.universe._

        val validation = reify( e.splice.validate( value.splice ) )
        val expression = c.Expr[ValidatedNel[String, V#Output]]( c.untypecheck( validation.tree ) )

        c.eval( expression ) match {
            case Valid( value ) ⇒
                c.Expr[V#Input Obeys V](
                    q"io.taig.gandalf.typelevel.Obeys[$v#Input, $v]( ${Literal( Constant( value ) )} )"
                )
            case Invalid( errors ) ⇒
                val messages = errors.toList.mkString( "\n - ", "\n - ", "" )

                c.abort(
                    c.enclosingPosition,
                    s"Can not lift value '${c.eval( value )}' into ${v.tpe}:$messages"
                )
        }
    }
}