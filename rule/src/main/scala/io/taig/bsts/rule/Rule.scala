package io.taig.bsts.rule

import io.taig.bsts.data.Validated
import io.taig.bsts.data.Validated.{ Invalid, Valid }
import io.taig.bsts.{ Error, Term }
import shapeless._
import shapeless.ops.hlist.ToTraversable
import shapeless.ops.hlist.ToTraversable.Aux

abstract class Rule[N <: String, T, A <: HList](
        implicit
        w: Witness.Aux[N]
) extends Term[N, T, T, A] {
    override final type V = Rule[N, T, A] :: HNil

    override final type R = Validated[Error[N, A], T]

    override def validations = this :: HNil
}

object Rule {
    def apply[T]( name: String ): Builder1[name.type, T] = new Builder1()( Witness.mkWitness( name ) )

    class Builder1[N <: String, T]( implicit w: Witness.Aux[N] ) {
        def apply( f: T ⇒ Boolean ): Rule[N, T, HNil] with Chain1[N, T] = new Rule[N, T, HNil] with Chain1[N, T] {
            override def validate( input: T ) = f( input ) match {
                case true  ⇒ Valid( input )
                case false ⇒ Invalid( Error( HNil ) )
            }

            override def apply[A <: HList]( g: T ⇒ A )(
                implicit
                tt: Aux[A, List, Any]
            ): Rule[N, T, A] = new Rule[N, T, A] {
                override def validate( input: T ) = f( input ) match {
                    case true  ⇒ Valid( input )
                    case false ⇒ Invalid( Error( g( input ) ) )
                }
            }
        }
    }

    trait Chain1[N <: String, T] {
        def apply[A <: HList]( f: T ⇒ A )(
            implicit
            tt: ToTraversable.Aux[A, List, Any]
        ): Rule[N, T, A]
    }

    def apply[T, U]( name: String ): Builder2[name.type, T, U] = new Builder2()( Witness.mkWitness( name ) )

    class Builder2[N <: String, T, U]( implicit w: Witness.Aux[N] ) {
        def apply( g: T ⇒ U )( f: U ⇒ Boolean ): Rule[N, T, HNil] with Chain2[N, T, U] = {
            new Rule[N, T, HNil] with Chain2[N, T, U] {
                override def validate( input: T ) = f( g( input ) ) match {
                    case true  ⇒ Valid( input )
                    case false ⇒ Invalid( Error( HNil ) )
                }

                override def apply[A <: HList]( h: ( T, U ) ⇒ A )(
                    implicit
                    tt: ToTraversable.Aux[A, List, Any]
                ): Rule[N, T, A] = new Rule[N, T, A] {
                    override def validate( input: T ) = {
                        val transformed = g( input )

                        f( transformed ) match {
                            case true  ⇒ Valid( input )
                            case false ⇒ Invalid( Error( h( input, transformed ) ) )
                        }
                    }
                }
            }
        }
    }

    trait Chain2[N <: String, T, U] {
        def apply[A <: HList]( f: ( T, U ) ⇒ A )(
            implicit
            tt: ToTraversable.Aux[A, List, Any]
        ): Rule[N, T, A]
    }
}