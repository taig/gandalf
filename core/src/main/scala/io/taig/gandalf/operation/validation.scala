package io.taig.gandalf.operation

import cats.data._
import io.taig.gandalf._

final class validation[V <: Validatable] {
    def validate( input: V#Input )(
        implicit
        v: Validation[V]
    ): ValidatedNel[String, V#Output] = v.validate( input )
}