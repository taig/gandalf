package io.taig.gandalf

import cats.data.Validated._
import cats.data._
import io.taig.gandalf.syntax.aliases._

/**
 * Type class that describes how to evaluate a Validation
 *
 * An instance of this type class must be defined for every user defined Validation.
 */
trait Evaluation[V <: Validation] {
    def validate( input: V#Input )( implicit e: Error[V] ): Result[V#Output]
}

object Evaluation {
    @inline
    def apply[V <: Validation]( implicit e: Evaluation[V] ): Evaluation[V] = e

    def instance[V <: Validation]( f: V#Input ⇒ Result[V#Output] ): Evaluation[V] = {
        new Evaluation[V] {
            override def validate( input: V#Input )( implicit e: Error[V] ) = f( input )
        }
    }

    def mutation[M <: Mutation]( f: M#Input ⇒ Option[M#Output] )( g: M#Input ⇒ M#Arguments ): Evaluation[M] = {
        new Evaluation[M] {
            override def validate( input: M#Input )( implicit e: Error[M] ) = {
                Validated.fromOption( f( input ), e.error( g( input ) ) )
            }
        }
    }

    def rule[R <: Rule]( f: R#Input ⇒ Boolean )( g: R#Input ⇒ R#Arguments ): Evaluation[R] = {
        new Evaluation[R] {
            override def validate( input: R#Input )( implicit e: Error[R] ) = {
                f( input ) match {
                    case true  ⇒ valid( input )
                    case false ⇒ invalid( e.error( g( input ) ) )
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