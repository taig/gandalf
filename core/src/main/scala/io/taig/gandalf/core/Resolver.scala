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

    def instance[R <: Rule, Out0 <: Rule]: Aux[R, Out0] =
        new Resolver[R] { override type Out = Out0 }

    implicit def entities[E <: Entity]: Resolver.Aux[E, E] = instance
}