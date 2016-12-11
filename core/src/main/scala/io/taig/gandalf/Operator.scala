package io.taig.gandalf

import scala.language.higherKinds

trait Operator extends Rule

object Operator {
    private[gandalf] trait Implicits[O[L <: Rule, R <: Rule] <: Operator, A <: Rule, B <: Rule] {
        def symbol: String

        implicit def entities[L <: Rule, R <: Rule, E <: A](
            implicit
            l: Resolver.Aux[L, E],
            r: Resolver.Aux[R, E],
            s: Serialization[L O R],
            e: Serialization[E]
        ): Resolver.Aux[L O R, E] = Resolver.instance[L O R, E]

        implicit def conditionTransformation[L <: Rule, R <: Rule](
            implicit
            l: Resolver.Aux[L, Rule.Condition],
            r: Resolver.Aux[R, _ <: B],
            s: Serialization[L O R]
        ): Resolver.Aux[L O R, Rule.Mutation] =
            Resolver.instance[L O R, Rule.Mutation]

        implicit def mutationCondition[L <: Rule, R <: Rule](
            implicit
            l: Resolver.Aux[L, _ <: B],
            r: Resolver.Aux[R, Rule.Condition],
            s: Serialization[L O R]
        ): Resolver.Aux[L O R, Rule.Mutation] =
            Resolver.instance[L O R, Rule.Mutation]

        implicit def serialization[L <: Rule, R <: Rule](
            implicit
            l: Serialization[L],
            r: Serialization[R]
        ): Serialization[L O R] = Serialization.instance[L O R]( s"($l $symbol $r)" )
    }
}

