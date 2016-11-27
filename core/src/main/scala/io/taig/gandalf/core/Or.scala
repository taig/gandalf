package io.taig.gandalf.core

class Or[L <: Rule, R <: Rule] extends Rule.Operator

object Or extends OrResolvers {
    implicit def validation[L <: Rule, R <: Rule, I, O](
        implicit
        l: Validation[L, I, O],
        r: Validation[R, I, O]
    ): Validation[L || R, I, O] = Validation.instance { input ⇒
        l( input ) orElse r( input )
    }

    implicit def serialization[L <: Rule, R <: Rule](
        implicit
        l: Serialization[L],
        r: Serialization[R]
    ): Serialization[L || R] = Serialization.instance( s"($l || $r)" )
}

trait OrResolvers {
    /**
     * condition[T] || condition[T] -> condition[T]
     */
    implicit def conditions[L <: Rule, R <: Rule, T](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Condition],
        lv: Validation[L, T, T],
        rr: Resolver.Aux[R, _ <: Rule.Condition],
        rv: Validation[R, T, T]
    ): Resolver.Aux[L || R, Rule.Condition] = Resolver.instance

    /**
     * mutation[I, O] || mutation[I, O] -> mutation[I, O]
     */
    implicit def mutations[L <: Rule, R <: Rule, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Mutation],
        lv: Validation[L, I, O],
        rr: Resolver.Aux[R, _ <: Rule.Mutation],
        rv: Validation[R, I, O]
    ): Resolver.Aux[L || R, Rule.Mutation] = Resolver.instance

    /**
     * condition[T] || mutation[T, T] -> mutation[T, T]
     */
    implicit def conditionMutation[L <: Rule, R <: Rule, T](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Condition],
        lv: Validation[L, T, T],
        rr: Resolver.Aux[R, _ <: Rule.Mutation],
        rv: Validation[R, T, T]
    ): Resolver.Aux[L || R, Rule.Mutation] = Resolver.instance

    /**
     * mutation[T, T] || condition[T] -> mutation[T, T]
     */
    implicit def mutationCondition[L <: Rule, R <: Rule, T](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Mutation],
        lv: Validation[L, T, T],
        rr: Resolver.Aux[R, _ <: Rule.Condition],
        rv: Validation[R, T, T]
    ): Resolver.Aux[L || R, Rule.Mutation] = Resolver.instance
}