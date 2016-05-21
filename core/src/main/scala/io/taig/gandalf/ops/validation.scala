package io.taig.gandalf.ops

import cats.data.Validated
import io.taig.gandalf._

class validation[V <: Validation]( validation: V )(
        implicit
        ev: Evaluation[V],
        er: Error[V]
) {
    def validate( value: V#Input ): Validated[List[String], V#Output] = ev.validate( value )
}