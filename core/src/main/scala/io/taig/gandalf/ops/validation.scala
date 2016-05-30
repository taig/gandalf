package io.taig.gandalf.ops

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class validation[V <: Validation]( validation: V ) {
    def validate( value: V#Input )( implicit ev: Evaluation[V], er: Error[V] ): Result[V#Output] = ev.validate( value )

    def as( message: String ): MessageValidation[V] = MessageValidation( validation, Error.instance( message ) )
}