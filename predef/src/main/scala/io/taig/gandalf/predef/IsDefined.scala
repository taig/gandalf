package io.taig.gandalf.predef

import io.taig.gandalf.data.Mutation
import io.taig.gandalf.{ Error, Validation }

class IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T

    override type Arguments = Error.Input[IsDefined[T]]
}

object IsDefined {
    implicit def validation[T](
        implicit
        e: Error[IsDefined[T]]
    ) = Validation.mutation[T, IsDefined[T]]( Predef.identity )( Error.input( _ ) )

    def isDefined[T]: IsDefined[T] = new IsDefined[T]
}