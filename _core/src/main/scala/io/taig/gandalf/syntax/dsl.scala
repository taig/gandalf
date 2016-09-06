package io.taig.gandalf.syntax

import io.taig.gandalf._
import io.taig.gandalf.data.{ Mutation, Rule, Transformation }

import scala.language.implicitConversions

trait dsl {
    implicit def mutationSyntax[I, O, M <: Mutation.Aux[I, O]](
        mutation: M with Mutation.Aux[I, O]
    ): operation.alteration[I, O, M] = {
        new operation.alteration[I, O, M]( mutation )
    }

    implicit def ruleSyntax[T, R <: Rule.Aux[T]](
        rule: R with Rule.Aux[T]
    ): operation.rule[T, R] = {
        new operation.rule[T, R]( rule )
    }

    implicit def transformationSyntax[I, O, T <: Mutation.Aux[I, O]](
        transformation: T with Transformation.Aux[I, O]
    ): operation.transformation[I, O, T] = {
        new operation.transformation[I, O, T]( transformation )
    }
}

object dsl extends dsl