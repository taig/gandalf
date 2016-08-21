package io.taig.gandalf

import cats.data.Validated._
import cats.data._

sealed trait Validation[V <: Validatable] {
    def validate( input: V#Input ): Result[V]
}

object Validation {
    @inline
    def apply[V <: Validatable](
        implicit
        v: Validation[V]
    ): Validation[V] = v

    private def instance[V <: Validatable](
        f: V#Input ⇒ Result[V]
    ): Validation[V] = {
        new Validation[V] {
            override def validate( input: V#Input ) = f( input )
        }
    }

    def mutation[M <: Mutation]( f: M#Input ⇒ Option[M#Output] )(
        g: M#Input ⇒ M#Arguments
    )(
        implicit
        e: Error[M]
    ): Validation[M] = instance { input ⇒
        f( input ).fold[Result[M]]( invalid( e.show( g( input ) ) ) )( valid )
    }

    def operation[O <: Operator]( f: O#Input ⇒ Result[O] )(
        g: ( O#Input, NonEmptyList[String] ) ⇒ O#Arguments
    )(
        implicit
        e: Error[O]
    ): Validation[O] = instance { input ⇒
        f( input ).leftMap( errors ⇒ e.show( g( input, errors ) ) )
    }

    /**
     * Construct a Rule Validation definition
     *
     * @param f Check the input value and return `true` if validations passes
     * @param g Prepare values for Error rendering, in most cases this may
     *          simply be the `identity` function
     */
    def rule[R <: Rule]( f: R#Input ⇒ Boolean )( g: R#Input ⇒ R#Arguments )(
        implicit
        e: Error[R]
    ): Validation[R] = instance { input ⇒
        f( input ) match {
            case true  ⇒ valid( input )
            case false ⇒ invalid( e.show( g( input ) ) )
        }
    }

    def transformation[M <: Mutation](
        f: M#Input ⇒ M#Output
    ): Validation[M] = instance { input ⇒ valid( f( input ) ) }
}