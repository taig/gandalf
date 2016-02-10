package io.taig.bsts

import shapeless._
import shapeless.ops.hlist.ToTraversable

import scala.PartialFunction.condOpt

abstract class Rule[N <: String, T, A <: HList]( implicit w: Witness.Aux[N] ) extends Validation[T, T] {
    override type F = Error[N, A]

    def name: String = w.value

    override final def validate( value: T ) = check( value ) match {
        case None          ⇒ Success( value )
        case Some( error ) ⇒ Failure( error )
    }

    protected def check( value: T ): Option[Error[N, A]]

    override def toString = name
}

object Rule {
    def apply[T]( name: String ): Builder1[name.type, T] = new Builder1[name.type, T]()( Witness.mkWitness( name ) )

    trait Chain1[N <: String, T] {
        def apply[A <: HList]( args: T ⇒ A )( implicit tt: ToTraversable.Aux[A, List, Any] ): Rule[N, T, A]
    }

    class Builder1[N <: String, T]( implicit w: Witness.Aux[N] ) {
        def apply( predicate: T ⇒ Boolean ): Rule[N, T, HNil] with Chain1[N, T] = {
            new Rule[N, T, HNil] with Chain1[N, T] {
                override def check( value: T ): Option[Error[N, HNil]] = {
                    condOpt( predicate( value ) ) { case false ⇒ Error( HNil: HNil ) }
                }

                override def apply[H <: HList]( args: T ⇒ H )(
                    implicit
                    tt: ToTraversable.Aux[H, List, Any]
                ): Rule[N, T, H] = new Rule[N, T, H] {
                    override def check( value: T ): Option[Error[N, H]] = {
                        condOpt( predicate( value ) ) { case false ⇒ Error( args( value ) ) }
                    }
                }
            }
        }
    }

    def apply[T, U]( name: String )( transformation: T ⇒ U ) = {
        new Builder2[name.type, T, U]( transformation )( Witness.mkWitness( name ) )
    }

    trait Chain2[N <: String, T, U] {
        def apply[A <: HList]( args: ( T, U ) ⇒ A )( implicit tt: ToTraversable.Aux[A, List, Any] ): Rule[N, T, A]
    }

    class Builder2[N <: String, T, U]( transformation: T ⇒ U )( implicit w: Witness.Aux[N] ) {
        def apply( predicate: U ⇒ Boolean ): Rule[N, T, HNil] with Chain2[N, T, U] = {
            new Rule[N, T, HNil] with Chain2[N, T, U] {
                override def check( value: T ): Option[Error[N, HNil]] = {
                    condOpt( predicate( transformation( value ) ) ) { case false ⇒ Error( HNil: HNil ) }
                }

                override def apply[H <: HList]( args: ( T, U ) ⇒ H )(
                    implicit
                    tt: ToTraversable.Aux[H, List, Any]
                ): Rule[N, T, H] = new Rule[N, T, H] {
                    override def check( value: T ): Option[Error[N, H]] = {
                        val transformed = transformation( value )

                        condOpt( predicate( transformed ) ) {
                            case false ⇒ Error( args( value, transformed ) )
                        }
                    }
                }
            }
        }
    }
}