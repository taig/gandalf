package io.taig.gandalf.core.test

import io.taig.gandalf.core.{ Transformation, Validation }

class generic[T] extends Transformation.With[T, T]( identity )

object generic {
    def instance[T]: generic[T] = new generic[T]

    implicit def validation[T]: Validation[generic[T]] = {
        new Validation[generic[T]] {
            override def validate( input: T ) = instance[T]( input )
        }
    }
}