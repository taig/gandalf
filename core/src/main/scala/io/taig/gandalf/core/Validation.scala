package io.taig.gandalf.core

import scala.PartialFunction.condOpt

trait Validation[-R <: Rule, I, O] {
    def confirm( input: I ): Option[O]
}

object Validation {
    @inline
    def apply[R <: Rule, I, O](
        implicit
        v: Validation[R, I, O]
    ): Validation[R, I, O] = v

    def instance[R <: Rule, I, O]( f: I ⇒ Option[O] ): Validation[R, I, O] = {
        new Validation[R, I, O] {
            override def confirm( input: I ): Option[O] = f( input )
        }
    }

    def condition[C <: Rule.Condition, T](
        f: T ⇒ Boolean
    ): Validation[C, T, T] = instance { input ⇒
        condOpt( f( input ) ) { case true ⇒ input }
    }

    @inline
    def mutation[M <: Rule.Mutation, I, O](
        f: I ⇒ Option[O]
    ): Validation[M, I, O] = instance( f )

    def transition[T <: Rule.Transition, I, O](
        f: I ⇒ O
    ): Validation[T, I, O] = instance( f andThen Some.apply )
}