package io.taig.gandalf

import cats.data.NonEmptyList

class Asdf extends Validatable {
    type Left <: Mutation

    type Right <: Validatable.Input[Left#Output]

    override type Input = Left#Input

    override type Output = Input

    override type Arguments = ( Input, NonEmptyList[String] )
}

object Asdf {
    implicit def validation[A <: Asdf](
        implicit
        vl: Validation[A#Left],
        vr: Validation[A#Right],
        e:  Error[A]
    ): Validation[A] = new Validation[A] {
        override def validate( input: A#Input ) = {
            vl.validate( input ).andThen { output ⇒
                vr.validate( output.asInstanceOf[A#Right#Input] )
            }.bimap( e.show( input, _ ), _ ⇒ input )
        }
    }
}