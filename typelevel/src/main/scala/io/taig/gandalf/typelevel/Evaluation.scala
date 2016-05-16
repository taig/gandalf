package io.taig.gandalf.typelevel

import cats.data.Validated._
import cats.data._

/**
 * Type class that describes how to evaluate a Validation
 *
 * An instance of this type class must be defined for every user defined Validation.
 */
trait Evaluation[V <: Validation] {
    def validate( input: V#Input )( implicit e: Error[V] ): ValidatedNel[String, V#Output]
}

object Evaluation {
    def apply[V <: Validation: Evaluation]: Evaluation[V] = Evaluation[V]

    def instance[V <: Validation]( f: V#Input ⇒ ValidatedNel[String, V#Output] ): Evaluation[V] = {
        new Evaluation[V] {
            override def validate( input: V#Input )( implicit e: Error[V] ) = f( input )
        }
    }

    def transformation[T <: Transformation]( f: T#Input ⇒ T#Output )( implicit e: Error[T] ): Evaluation[T] = {
        instance { input ⇒
            valid( f( input ) )
        }
    }
}