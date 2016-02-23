package io.taig.bsts

import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.ops.Extraction
import shapeless._

import scala.language.higherKinds

abstract class Mutation[N <: String, I, O, A <: HList](
        implicit
        w: Witness.Aux[N]
) extends Term[N, I, O, A] {
    override type V = Mutation[N, I, O, A] :: HNil

    override type E = Error[N, A]

    override def validations: V = this :: HNil
}

object Mutation {
    def apply[I, O]( name: String ): Builder1[name.type, I, O] = new Builder1()( Witness.mkWitness( name ) )

    class Builder1[N <: String, I, O]( implicit w: Witness.Aux[N] ) {
        def apply[F[_]]( f: I ⇒ F[O] )(
            implicit
            e: Extraction[O, F]
        ): Mutation[N, I, O, HNil] with Chain1[N, I, O] = new Mutation[N, I, O, HNil] with Chain1[N, I, O] {
            override def validate( input: I ) = e.extract( f( input ) ) match {
                case Some( output ) ⇒ Valid( output )
                case None           ⇒ Invalid( Error( HNil: HNil ) )
            }

            override def apply[A <: HList]( g: I ⇒ A ): Mutation[N, I, O, A] = new Mutation[N, I, O, A] {
                override def validate( input: I ) = e.extract( f( input ) ) match {
                    case Some( output ) ⇒ Valid( output )
                    case None           ⇒ Invalid( Error( g( input ) ) )
                }
            }
        }
    }

    trait Chain1[N <: String, I, O] {
        def apply[A <: HList]( f: I ⇒ A ): Mutation[N, I, O, A]
    }
}