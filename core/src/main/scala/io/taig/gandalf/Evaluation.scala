package io.taig.gandalf

import cats.data.Validated._
import cats.data._

/**
 * Type class that describes how to evaluate a Validation
 *
 * An instance of this type class must be defined for every user defined Validation.
 */
trait Evaluation[V <: Validation] {
    def validate( input: V#Input )( implicit e: Error[V] ): Validated[List[String], V#Output]
}

object Evaluation {
    def apply[V <: Validation: Evaluation]: Evaluation[V] = Evaluation[V]

    def instance[V <: Validation]( f: V#Input ⇒ Validated[List[String], V#Output] )( implicit e: Error[V] ): Evaluation[V] = {
        new Evaluation[V] {
            override def validate( input: V#Input )( implicit e: Error[V] ) = f( input )
        }
    }

    def mutation[M <: Mutation: Error]( f: M#Input ⇒ Option[M#Output] )( implicit e: Error[M] ): Evaluation[M] = {
        new Evaluation[M] {
            override def validate( input: M#Input )( implicit e: Error[M] ) = {
                Validated.fromOption( f( input ), e.error.toList )
            }
        }
    }

    def rule[R <: Rule]( condition: R#Input ⇒ Boolean )( implicit e: Error[R] ): Evaluation[R] = {
        new Evaluation[R] {
            override def validate( input: R#Input )( implicit e: Error[R] ) = {
                condition( input ) match {
                    case true  ⇒ valid( input )
                    case false ⇒ invalid( e.error.toList )
                }
            }
        }
    }

    def transformation[T <: Transformation]( f: T#Input ⇒ T#Output ): Evaluation[T] = {
        new Evaluation[T] {
            override def validate( input: T#Input )( implicit e: Error[T] ) = valid( f( input ) )
        }
    }
}