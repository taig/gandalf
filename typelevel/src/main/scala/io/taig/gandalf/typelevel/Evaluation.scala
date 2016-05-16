package io.taig.gandalf.typelevel

import cats.data.Validated._
import cats.data._

/**
 * Type class that describes how to evaluate a Validation
 *
 * An instance of this type class must be defined for every user defined Validation.
 */
trait Evaluation[V <: Validation] {
    def validate( input: V#Input ): Validated[List[String], V#Output]
}

object Evaluation {
    def apply[V <: Validation: Evaluation]: Evaluation[V] = Evaluation[V]

    def instance[V <: Validation]( f: V#Input ⇒ Validated[List[String], V#Output] ): Evaluation[V] = new Evaluation[V] {
        override def validate( input: V#Input ) = f( input )
    }

    def mutation[M <: Mutation: Error]( f: M#Input ⇒ Option[M#Output] ): Evaluation[M] = {
        new Evaluation[M] {
            override def validate( input: M#Input ) = Validated.fromOption( f( input ), Error[M].error.toList )
        }
    }

    def rule[R <: Rule: Error]( condition: R#Input ⇒ Boolean ): Evaluation[R] = {
        new Evaluation[R] {
            override def validate( input: R#Input ) = {
                condition( input ) match {
                    case true  ⇒ valid( input )
                    case false ⇒ invalid( Error[R].error.toList )
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