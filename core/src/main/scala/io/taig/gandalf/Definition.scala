package io.taig.gandalf

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

class Definition extends StaticAnnotation {
    def macroTransform( annottees: Any* ): Any = macro Definition.apply
}

private object Definition {
    def apply( c: blackbox.Context )( annottees: c.Expr[Any]* ): c.Expr[Any] = {
        val helper = new Helper[c.type]( c )

        import c.universe._
        import helper._

        val tree = annottees.head.tree

        val result = tree match {
            case c @ ClassDef( _, name, _, _ ) if isSingletonDefinition( c ) ⇒
                q"""
                $c

                object ${name.toTermName} extends $name {
                    implicit def validation[V <: $name]:
                        io.taig.gandalf.Validation[V] = {
                        io.taig.gandalf.Validation.instance[V] { input =>
                            ${name.toTermName}( input )
                        }
                   }
                }
                """
            case c @ ClassDef( _, name, tparams, _ ) if isTypeParamDefinition( c ) ⇒
                q"""
                $c

                object ${name.toTermName} {
                    @scala.inline
                    def apply[..$tparams]: $name[..${tparams.map( _.name )}] = {
                        new $name[..${tparams.map( _.name )}]
                    }

                    implicit def validation[..$tparams, __X[..$tparams] <: $name[..${tparams.map( _.name )}]]:
                        io.taig.gandalf.Validation[__X[..${tparams.map( _.name )}]] = {
                        io.taig.gandalf.Validation.instance[__X[..${tparams.map( _.name )}]] { input =>
                            apply[..${tparams.map( _.name )}]( input )
                        }
                    }
                }
                """
            case c @ ClassDef( _, name, tparams, impl ) if isValueTypeParamDefinition( c ) ⇒
                val constructor = constructors( c ) match {
                    case constructor :: Nil ⇒ constructor
                    case Nil ⇒ context.abort(
                        context.enclosingPosition,
                        "No constructor"
                    )
                    case _ ⇒ context.abort(
                        context.enclosingPosition,
                        "Too many constructors"
                    )
                }

                val parameters = constructor.vparamss match {
                    case Nil :: implicits :: Nil if implicits.forall( _.mods.hasFlag( Flag.IMPLICIT ) ) ⇒
                        implicits
                    case _ ⇒ context.abort(
                        context.enclosingPosition,
                        "Only implicit constructor parameters allowed"
                    )
                }

                val implicits = tparams.zipWithIndex.map {
                    case ( t, i ) ⇒
                        Typed( Ident( TermName( s"ev$i" ) ), tq"ValueOf[${t.name}]" )
                }

                q"""
                $c

                object ${name.toTermName} {
                    @scala.inline
                    def apply[..$tparams]( implicit ..$parameters ):
                        $name[..${tparams.map( _.name )}] = {
                        new $name[..${tparams.map( _.name )}]
                    }

                    implicit def validation[..$tparams, __X[..$tparams] <: $name[..${tparams.map( _.name )}]](
                        implicit
                        ..$implicits
                    ): io.taig.gandalf.Validation[__X[..${tparams.map( _.name )}]] = {
                        io.taig.gandalf.Validation.instance[__X[..${tparams.map( _.name )}]] { input =>
                            apply[..${tparams.map( _.name )}].apply( input )
                        }
                    }
                }
                """
            case _ ⇒ c.abort( c.enclosingPosition, "Invalid definition" )
        }

        c.Expr( result )
    }
}

private case class Helper[C <: blackbox.Context]( context: C ) {
    import context.universe._

    def isSingletonDefinition( classDef: ClassDef ): Boolean = {
        classDef.tparams.isEmpty
    }

    def isTypeParamDefinition( classDef: ClassDef ): Boolean = {
        classDef.tparams.nonEmpty && constructors( classDef )
            .flatMap( _.vparamss )
            .forall( _.isEmpty )
    }

    def isValueTypeParamDefinition( classDef: ClassDef ): Boolean = {
        classDef.tparams.nonEmpty && constructors( classDef )
            .flatMap( _.vparamss )
            .exists( _.nonEmpty )
    }

    def constructors( classDef: ClassDef ): List[DefDef] = {
        classDef.impl.collect {
            case constructor @ DefDef( _, termNames.CONSTRUCTOR, _, _, _, _ ) ⇒
                constructor
        }
    }

    def dropFlags( valDef: ValDef ): ValDef = {
        import valDef._
        ValDef( Modifiers(), name, tpt, rhs )
    }

    def unbox( valDef: ValDef ): ValDef = {
        import valDef._

        val tpe = valDef.tpt match {
            case AppliedTypeTree( _, tpe :: Nil ) ⇒
                tpe
        }

        ValDef( mods, name, tpe, rhs )
    }
}