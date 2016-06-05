package io.taig.gandalf.syntax

import io.taig.gandalf._
import io.taig.gandalf.data.Transformation

import scala.language.implicitConversions

trait transformation {
    implicit def transformationSyntax[I, O, T <: Transformation.Aux[I, O]](
        transformation: T with Transformation.Aux[I, O]
    ): operation.transformation[I, O, T] = {
        new operation.transformation[I, O, T]( transformation )
    }
}

object transformation extends transformation