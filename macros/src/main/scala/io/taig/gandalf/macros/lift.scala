package io.taig.gandalf.macros

import cats.data.Validated.{ Invalid, Valid }
import io.taig.gandalf.core._
import shapeless.{ Lazy, Strict }

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.util.{ Success, Try }

object lift {
    def apply[I, C <: Container { type Kind <: Rule.Input[I] }]( input: I )(
        rule: C with Container { type Kind <: Rule.Input[I] }
    )(
        implicit
        v: Validation[C]
    ): I Obey C = macro inference[I, C]

    def inference[I, C <: Container { type Kind <: Rule.Input[I] }](
        c: blackbox.Context
    )(
        input: c.Expr[I]
    )(
        rule: c.Expr[C with Container { type Kind <: Rule.Input[I] }]
    )(
        v: c.Expr[Validation[C]]
    )(
        implicit
        wtti: c.WeakTypeTag[I],
        wttr: c.WeakTypeTag[C]
    ): c.Expr[I Obey C] = implementation[I, C]( c )( input )( v )

    def implementation[I, C <: Container { type Kind <: Rule.Input[I] }](
        c: blackbox.Context
    )(
        input: c.Expr[I]
    )(
        v: c.Expr[Validation[C]]
    )(
        implicit
        wtti: c.WeakTypeTag[I],
        wttr: c.WeakTypeTag[C]
    ): c.Expr[I Obey C] = {
        import c.universe._

        val validation = reify( v.splice.validate( input.splice ) )
        val expression = c.Expr[Result[C]]( c.untypecheck( validation.tree ) )

        def validationType = tryN( 2, c.eval {
            c.Expr[String] {
                c.untypecheck( reify( input.splice.toString ).tree )
            }
        } )

        tryN( 2, c.eval( expression ) ) match {
            case Valid( _ ) ⇒ c.Expr[I Obey C](
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

    def tryN[T]( n: Int, t: ⇒ T ): T = {
        Stream
            .fill( n )( Try( t ) )
            .collectFirst { case Success( r ) ⇒ r }
            .getOrElse( t )
    }
}