package io.taig.gandalf

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.data._
import io.taig.gandalf.syntax.aliases._

trait Validation[O, -A <: Action.Output[O]] {
    def validate( input: A#Input ): Result[O]
}

object Validation {
    @inline
    def apply[O, A <: Action.Output[O]]( implicit v: Validation[O, A] ): Validation[O, A] = v

    def instance[O, A <: Action.Output[O]]( f: A#Input ⇒ Result[O] ): Validation[O, A] = {
        new Validation[O, A] {
            override def validate( input: A#Input ) = f( input )
        }
    }

    def mutation[O, M <: Mutation.Output[O]]( f: M#Input ⇒ Option[O] )( args: M#Input ⇒ M#Arguments )(
        implicit
        e: Error[M]
    ): Validation[O, M] = {
        new Validation[O, M] {
            override def validate( input: M#Input ) = f( input ) match {
                case Some( output ) ⇒ valid( output )
                case None           ⇒ invalid( e.error( args( input ) ) )
            }
        }
    }

    def operation[P, L <: Action, R <: Action.Aux[L#Output, P], O <: Operation[L, R]](
        f: L#Input ⇒ Result[P]
    )(
        args: ( L#Input, NonEmptyList[String] ) ⇒ Error.Forward[O]
    )(
        implicit
        e: Error[O]
    ): Validation[P, O] = {
        new Validation[P, O] {
            override def validate( input: L#Input ) = {
                f( input ).leftMap( errors ⇒ e.error( args( input, errors ) ) )
            }
        }
    }

    def rule[T, R <: Rule.Aux[T]]( f: T ⇒ Boolean )( args: T ⇒ R#Arguments )(
        implicit
        e: Error[R]
    ): Validation[T, R] = {
        new Validation[T, R] {
            override def validate( input: T ) = f( input ) match {
                case true  ⇒ valid( input )
                case false ⇒ invalid( e.error( args( input ) ) )
            }
        }
    }

    def transformation[O, T <: Transformation.Output[O]]( f: T#Input ⇒ O ): Validation[O, T] = {
        new Validation[O, T] {
            override def validate( input: T#Input ) = valid( f( input ) )
        }
    }
}