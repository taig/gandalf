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
    def validate( input: V#Input ): Result[V#Output]
}

object Evaluation {
    @inline
    def apply[V <: Validation: Evaluation]: Evaluation[V] = Evaluation[V]

    def instance[V <: Validation]( f: V#Input ⇒ Result[V#Output] ): Evaluation[V] = {
        new Evaluation[V] {
            override def validate( input: V#Input ) = f( input )
        }
    }

    def mutation[M <: Mutation: Error]( f: M#Input ⇒ Option[M#Output] )( g: M#Input ⇒ M#Arguments )(
        implicit
        e: Error[M]
    ): Evaluation[M] = {
        new Evaluation[M] {
            override def validate( input: M#Input ) = {
                Validated.fromOption( f( input ), e.error( g( input ) ) )
            }
        }
    }

    def rule[R <: Rule]( condition: R#Input ⇒ Boolean )( g: R#Input ⇒ R#Arguments )(
        implicit
        e: Error[R]
    ): Evaluation[R] = {
        new Evaluation[R] {
            override def validate( input: R#Input ) = {
                condition( input ) match {
                    case true  ⇒ valid( input )
                    case false ⇒ invalid( e.error( g( input ) ) )
                }
            }
        }
    }

    def transformation[T <: Transformation]( f: T#Input ⇒ T#Output ): Evaluation[T] = {
        new Evaluation[T] {
            override def validate( input: T#Input ) = valid( f( input ) )
        }
    }
}