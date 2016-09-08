package io.taig.gandalf.macros

import cats.data.Validated.{ Invalid, Valid }
import io.taig.gandalf.core._

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object lift {
    def apply[I, R <: Rule.Input[I]]( input: I )( rule: R with Rule.Input[I] )(
        implicit
        v: Validation[R]
    ): I Obey R = macro inference[I, R]

    def inference[I, R <: Rule.Input[I]](
        c: blackbox.Context
    )(
        input: c.Expr[I]
    )(
        rule: c.Expr[R with Rule.Input[I]]
    )(
        v: c.Expr[Validation[R]]
    )(
        implicit
        wtti: c.WeakTypeTag[I],
        wttr: c.WeakTypeTag[R]
    ): c.Expr[I Obey R] = implementation[I, R]( c )( input )( v )

    def implementation[I, R <: Rule.Input[I]](
        c: blackbox.Context
    )(
        input: c.Expr[I]
    )(
        v: c.Expr[Validation[R]]
    )(
        implicit
        wtti: c.WeakTypeTag[I],
        wttr: c.WeakTypeTag[R]
    ): c.Expr[I Obey R] = {
        import c.universe._

        val validation = reify( v.splice.validate( input.splice ) )
        val expression = c.Expr[Result[R]]( c.untypecheck( validation.tree ) )
        def validationType = c.eval {
            c.Expr[String] {
                c.untypecheck( reify( input.splice.toString ).tree )
            }
        }

        c.eval( expression ) match {
            case Valid( value ) ⇒ c.Expr[I Obey R](
                q"""
                _root_.io.taig.gandalf.macros.Obey[$wtti, $wttr](
                    $expression.getOrElse {
                        throw new _root_.java.lang.IllegalStateException(
                            "Runtime-validation failed. What the heck are you doing?!"
                        )
                    }
                )
                """
            )
            case Invalid( errors ) ⇒ c.abort(
                c.enclosingPosition,
                s"""
                   |Can not lift input '${show( input.tree )}' into $validationType:
                   |${errors.map( " - " + _ ).toList.mkString( "\n" )}
                 """.stripMargin.trim
            )
        }
    }
}