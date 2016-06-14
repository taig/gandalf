package io.taig.gandalf.internal

import cats.data.Validated.{ Invalid, Valid }
import cats.std.list._
import cats.syntax.foldable._
import io.taig.gandalf._
import io.taig.gandalf.data.{ Action, Obeys }
import io.taig.gandalf.syntax.aliases._

import scala.reflect.macros.whitebox

object Macro {
    def lift[A <: Action](
        c: whitebox.Context
    )(
        value: c.Expr[A#Input]
    )(
        v: c.Expr[Validation[_, A]]
    )(
        implicit
        wttv: c.WeakTypeTag[A]
    ): c.Expr[A#Input Obeys A] = {
        import c.universe._

        val validation = reify( v.splice.validate( value.splice ) )
        val expression = c.Expr[Result[A#Output]]( c.untypecheck( validation.tree ) )
        def validationType = c.eval( c.Expr[String]( c.untypecheck( reify( value.splice.toString ).tree ) ) )

        c.eval( expression ) match {
            case Valid( value ) ⇒
                c.Expr[A#Input Obeys A](
                    q"""io.taig.gandalf.data.Obeys[${wttv}#Input, $wttv](
                        $expression.getOrElse {
                            throw new IllegalStateException(
                                "Runtime-validation failed. What the heck are you doing?!"
                            )
                        }
                    )"""
                )
            case Invalid( errors ) ⇒
                val messages = errors.map( " - " + _ ).toList.mkString( "\n" )

                c.abort(
                    c.enclosingPosition,
                    s"Can not lift value '${show( value.tree )}' into $validationType:\n$messages"
                )
        }
    }

    def obeys( c: whitebox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        import c.universe._
        import termNames.CONSTRUCTOR

        def mutate( tree: Tree, injection: Tree ): Tree = tree match {
            case Apply( tree, args )                    ⇒ Apply( mutate( tree, injection ), args )
            case Select( ident @ Ident( _ ), operator ) ⇒ Select( Apply( injection, List( ident ) ), operator )
            case Select( tree, name )                   ⇒ Select( mutate( tree, injection ), name )
            case _                                      ⇒ tree
        }

        val trees = annottees.map( _.tree )
        val Expr( ValDef( _, _, input, _ ) ) = annottees.head
        val injection = Select(
            TypeApply(
                Select(
                    Select(
                        Select(
                            Select( Select( Ident( TermName( "io" ) ), TermName( "taig" ) ), TermName( "gandalf" ) ),
                            TermName( "internal" )
                        ),
                        TermName( "Identity" )
                    ),
                    TermName( "identity" )
                ),
                List( input )
            ),
            TermName( "$tilde$greater" )
        )

        val target = c.prefix.tree match {
            case q"new obeys[$validation]" ⇒ validation
            case q"new obeys( $validation )" ⇒
                c.typecheck {
                    q"""
                    import io.taig.gandalf.syntax.all._
                    ${mutate( validation, injection )}
                    """
                }
            case _ ⇒ c.abort(
                c.enclosingPosition,
                "Illegal @obeys format. Can bei either @obeys[Required] or @obeys( required )"
            )
        }

        def newType( lhs: Tree ) = tq"io.taig.gandalf.operator.Obeys[$lhs, $target]"

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
