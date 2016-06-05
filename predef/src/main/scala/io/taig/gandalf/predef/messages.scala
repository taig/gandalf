package io.taig.gandalf.predef

import io.taig.gandalf.{ Arguments, Error }
import io.taig.gandalf.data.{ Action, Mutation, Rule }

import scala.reflect._

object messages {
    implicit def errorMutation[M <: Mutation: ClassTag]: Error[M] = errorClass[M]

    implicit def errorRule[R <: Rule: ClassTag]: Error[R] = errorClass[R]

    private def errorClass[C <: Action with Arguments: ClassTag]: Error[C] = {
        Error.instance( classTag[C].runtimeClass.getSimpleName )
    }
}