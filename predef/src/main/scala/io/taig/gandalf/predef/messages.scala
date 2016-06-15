package io.taig.gandalf.predef

import io.taig.gandalf.data.{ Mutate, Mutation, Rule }
import io.taig.gandalf.{ Arguments, Error }
import shapeless.<:!<

import scala.reflect._

object messages {
    implicit def errorMutation[M <: Mutation: ClassTag](
        implicit
        ev: M <:!< Mutate
    ): Error[M] = errorClass[M]

    implicit def errorRule[R <: Rule: ClassTag]: Error[R] = errorClass[R]

    private def errorClass[C <: Arguments: ClassTag]: Error[C] = {
        Error.instance( classTag[C].runtimeClass.getSimpleName )
    }
}