package io.taig.gandalf.ops

import io.taig.gandalf._
import io.taig.gandalf.syntax.aliases._

class validation[V <: Validation]( validation: V ) {
    def validate( value: V#Input )( implicit e: Evaluation[V] ): Result[V#Output] = e.validate( value )

    def as( message: String ): MessageValidation[V] = MessageValidation( validation, Error.instance( message ) )
}