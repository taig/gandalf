package io.taig.gandalf.core

import io.taig.gandalf.core.Rule._

import scala.annotation.implicitNotFound
import scala.language.higherKinds

@implicitNotFound( "Invalid operation" )
trait Resolver[-R <: Rule] {
    type Out <: Rule
}

object Resolver {
    type Aux[-R <: Rule, Out0 <: Rule] = Resolver[R] {
        type Out = Out0
    }

    def instance[R <: Rule, Out0 <: Rule]: Aux[R, Out0] = new Resolver[R] {
        override type Out = Out0
    }

    implicit def condition[C <: Condition]: Resolver.Aux[C, Condition] =
        instance

    implicit def mutation[M <: Mutation]: Resolver.Aux[M, Mutation] =
        instance

    implicit def transitions[T <: Transition]: Resolver.Aux[T, Transition] =
        instance
}