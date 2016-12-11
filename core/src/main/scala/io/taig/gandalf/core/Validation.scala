package io.taig.gandalf.core

import scala.PartialFunction.condOpt

trait Validation[-R <: Rule, I] {
    type Out

    def confirm( input: I ): Option[Out]
}

object Validation {
    type Aux[-R <: Rule, I, O] = Validation[R, I] { type Out = O }

    @inline
    def apply[R <: Rule, I, O](
        implicit
        v: Validation.Aux[R, I, O]
    ): Validation.Aux[R, I, O] = v

    def instance[R <: Rule, I, O](
        f: I ⇒ Option[O]
    ): Validation.Aux[R, I, O] = new Validation[R, I] {
        override type Out = O

        override def confirm( input: I ): Option[O] = f( input )
    }

    def condition[C <: Rule.Condition, T](
        f: T ⇒ Boolean
    ): Validation.Aux[C, T, T] = instance { input ⇒
        condOpt( f( input ) ) { case true ⇒ input }
    }

    @inline
    def mutation[M <: Rule.Mutation, I, O](
        f: I ⇒ Option[O]
    ): Validation.Aux[M, I, O] = instance( f )

    def transition[T <: Rule.Transition, I, O](
        f: I ⇒ O
    ): Validation.Aux[T, I, O] = instance( f andThen Some.apply )
}