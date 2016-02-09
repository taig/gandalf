package io.taig.bsts

import shapeless._
import shapeless.ops.hlist.ToTraversable
import shapeless.ops.hlist.ToTraversable.Aux

abstract class Transformation[N <: String, I, O, A <: HList]( implicit w: Witness.Aux[N] ) extends Validation[I, O] {
    def name: String = w.value

    def transform( value: I ): Result[Error[N, A], O]

    override def toString = name
}

object Transformation {
    def apply[I, O]( name: String ): Builder1[name.type, I, O] = {
        new Builder1[name.type, I, O]()( Witness.mkWitness( name ) )
    }

    trait Chain1[N <: String, I, O] {
        def apply[A <: HList]( args: I ⇒ A )( implicit tt: ToTraversable.Aux[A, List, Any] ): Transformation[N, I, O, A]
    }

    class Builder1[N <: String, I, O]( implicit w: Witness.Aux[N] ) {
        def from( f: I ⇒ O ): Transformation[N, I, O, HNil] = new Transformation[N, I, O, HNil] {
            override def transform( input: I ): Result[Error[w.T, HNil], O] = Success( f( input ) )
        }

        def apply( f: I ⇒ Option[O] ): Transformation[N, I, O, HNil] with Chain1[N, I, O] = {
            new Transformation[N, I, O, HNil] with Chain1[N, I, O] {
                override def transform( input: I ): Result[Error[N, HNil], O] = f( input ) match {
                    case Some( output ) ⇒ Success( output )
                    case None           ⇒ Failure( Error( HNil: HNil ) )
                }

                override def apply[A <: HList]( args: I ⇒ A )(
                    implicit
                    tt: Aux[A, List, Any]
                ): Transformation[N, I, O, A] = new Transformation[N, I, O, A] {
                    override def transform( input: I ): Result[Error[N, A], O] = f( input ) match {
                        case Some( output ) ⇒ Success( output )
                        case None           ⇒ Failure( Error( args( input ) ) )
                    }
                }
            }
        }
    }
}