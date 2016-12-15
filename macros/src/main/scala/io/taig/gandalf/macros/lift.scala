package io.taig.gandalf.macros

import io.taig.gandalf._

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.util.{ Success, Try }

object lift {
    def fromType[R <: Rule, I, O]( input: I )(
        implicit
        v: Validation[R, I, O],
        s: Serialization[R]
    ): Obeys[R, I, O] = macro implementation[R, I, O]

    def fromRule[R <: Rule, I, O]( rule: R )( input: I )(
        implicit
        v: Validation[R, I, O],
        s: Serialization[R]
    ): Obeys[R, I, O] = macro inference[R, I, O]

    def inference[R <: Rule, I, O](
        c: blackbox.Context
    )(
        rule: c.Expr[R]
    )(
        input: c.Expr[I]
    )(
        v: c.Expr[Validation[R, I, O]],
        s: c.Expr[Serialization[R]]
    )(
        implicit
        wttr: c.WeakTypeTag[R],
        wtti: c.WeakTypeTag[I],
        wtto: c.WeakTypeTag[O]
    ): c.Expr[Obeys[R, I, O]] = implementation[R, I, O]( c )( input )( v, s )

    def implementation[R <: Rule, I, O](
        c: blackbox.Context
    )(
        input: c.Expr[I]
    )(
        v: c.Expr[Validation[R, I, O]],
        s: c.Expr[Serialization[R]]
    )(
        implicit
        wttr: c.WeakTypeTag[R],
        wtti: c.WeakTypeTag[I],
        wtto: c.WeakTypeTag[O]
    ): c.Expr[Obeys[R, I, O]] = {
        import c.universe._

        val validation = reify( v.splice.confirm( input.splice ) )
        val expression = c.Expr[Option[O]]( c.untypecheck( validation.tree ) )

        def validationType = tryN( 2, c.eval {
            c.Expr[String] {
                c.untypecheck( reify( s.splice.serialize ).tree )
            }
        } )

        tryN( 2, c.eval( expression ) ) match {
            case Some( _ ) ⇒ c.Expr[Obeys[R, I, O]](
                q"""
                _root_.io.taig.gandalf.Obeys.applyUnsafe[$wttr, $wtti, $wtto](
                    $expression.getOrElse {
                        throw new _root_.java.lang.IllegalStateException(
                            "Runtime-rule failed. What the heck are you doing?!"
                        )
                    }
                )
                """
            )
            case None ⇒ c.abort(
                c.enclosingPosition,
                s"Can not lift input '${show( input.tree )}' into $validationType"
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