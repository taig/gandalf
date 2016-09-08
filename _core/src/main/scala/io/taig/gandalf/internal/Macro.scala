package io.taig.gandalf.internal

import cats.data.Validated.{ Invalid, Valid }
import cats.std.list._
import cats.syntax.foldable._
import io.taig.gandalf._
import io.taig.gandalf.data.{ Action, Obeys }
import io.taig.gandalf.syntax.aliases._

import scala.reflect.macros.whitebox

object Macro {
    def liftInputAction[I, A <: Validation.Input[I]]( c: whitebox.Context )( value: c.Expr[I] )( v: c.Expr[Validation[_, A]] )(
        implicit
        wtta: c.WeakTypeTag[A]
    ): c.Expr[I Obeys A] = liftAction[A]( c )( value )( v )

    def liftActionRuntime[A <: Validation]( c: whitebox.Context )( action: c.Expr[A] )( value: c.Expr[A#Input] )( v: c.Expr[Validation[_, A]] )(
        implicit
        wtta: c.WeakTypeTag[A]
    ): c.Expr[A#Input Obeys A] = liftAction[A]( c )( value )( v )

    def liftAction[A <: Validation]( c: whitebox.Context )( value: c.Expr[A#Input] )( v: c.Expr[Validation[_, A]] )(
        implicit
        wtta: c.WeakTypeTag[A]
    ): c.Expr[A#Input Obeys A] = {
        import c.universe._

        val validation = reify( v.splice.validate( value.splice ) )
        val expression = c.Expr[Result[A#Output]]( c.untypecheck( validation.tree ) )
        def validationType = c.eval( c.Expr[String]( c.untypecheck( reify( value.splice.toString ).tree ) ) )

        c.eval( expression ) match {
            case Valid( value ) ⇒
                c.Expr[A#Input Obeys A](
                    q"""
                    _root_.io.taig.gandalf.data.Obeys[$wtta#Input, $wtta](
                        $expression.getOrElse {
                            throw new _root_.java.lang.IllegalStateException(
                                "Runtime-validation failed. What the heck are you doing?!"
                            )
                        }
                    )
                    """
                )
            case Invalid( errors ) ⇒
                val messages = errors.map( " - " + _ ).toList.mkString( "\n" )

                c.abort(
                    c.enclosingPosition,
                    s"Can not lift input '${show( value.tree )}' into $validationType:\n$messages"
                )
        }
    }

    def obeys( c: whitebox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
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
                "Illegal @obeys format. Can bei either @obeys[Trim <~> Required] or @obeys( Trim ~> Required )"
            )
        }

        def newType( lhs: Tree ) = tq"_root_.io.taig.gandalf.data.Obeys[$lhs, $target]"

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
    private def retype( c: whitebox.Context )( tree: c.Tree, input: c.Tree ): c.Tree = {
        import c.universe._

        tree match {
            case Apply( tree, args )  ⇒ Apply( retype( c )( tree, input ), args )
            case Select( tree, name ) ⇒ Select( retype( c )( tree, input ), name )
            case ident ⇒
                q"""
                new _root_.io.taig.gandalf.internal.DslInferenceHelper[$input].infer( $ident )
                """
        }
    }
}