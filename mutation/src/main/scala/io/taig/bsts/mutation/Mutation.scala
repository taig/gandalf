package io.taig.bsts.mutation

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.mutation.ops.Extraction
import io.taig.bsts.{ Error, Term }
import shapeless._
import shapeless.ops.hlist.ToTraversable

import scala.language.higherKinds

abstract class Mutation[N <: String, I, O, A <: HList](
        implicit
        w: Witness.Aux[N]
) extends Term[N, I, O, A] {
    override final type V = Mutation[N, I, O, A] :: HNil

    override final type R = Validated[Error[N, A], O]

    override final def validations: V = this :: HNil
}

object Mutation {
    def apply[I, O]( name: String ): Builder1[name.type, I, O] = new Builder1()( Witness.mkWitness( name ) )

    class Builder1[N <: String, I, O]( implicit w: Witness.Aux[N] ) {
        def apply[F[_]]( f: I ⇒ F[O] )(
            implicit
            e: Extraction[O, F]
        ): Mutation[N, I, O, HNil] with Chain1[N, I, O] = new Mutation[N, I, O, HNil] with Chain1[N, I, O] {
            override def validate( input: I ): R = e.extract( f( input ) ) match {
                case Some( output ) ⇒ Valid( output )
                case None           ⇒ Invalid( Error( HNil ) )
            }

            override def apply[A <: HList]( g: I ⇒ A )(
                implicit
                tt: ToTraversable.Aux[A, List, Any]
            ): Mutation[N, I, O, A] = new Mutation[N, I, O, A] {
                override def validate( input: I ): R = e.extract( f( input ) ) match {
                    case Some( output ) ⇒ Valid( output )
                    case None           ⇒ Invalid( Error( g( input ) ) )
                }
            }
        }
    }

    trait Chain1[N <: String, I, O] {
        def apply[A <: HList]( f: I ⇒ A )(
            implicit
            tt: ToTraversable.Aux[A, List, Any]
        ): Mutation[N, I, O, A]
    }
}