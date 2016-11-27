package io.taig.gandalf.core

class And[L <: Rule, R <: Rule] extends Rule.Operator

object And extends AndResolvers {
    implicit def validation[L <: Rule, R <: Rule, I, O, P](
        implicit
        l: Validation[L, I, O],
        r: Validation[R, O, P]
    ): Validation[L && R, I, P] = Validation.instance( l( _ ) flatMap r.apply )

    implicit def serialization[L <: Rule, R <: Rule](
        implicit
        l: Serialization[L],
        r: Serialization[R]
    ): Serialization[L && R] = Serialization.instance( s"($l && $r)" )
}

trait AndResolvers extends AndResolvers0 {
    //    /**
    //     * condition[T] && condition[T] -> condition[T]
    //     * mutation[I, O] && mutation[O, P] -> mutation[I, P]
    //     * transition[I, O] && transition[O, P] -> transition[I, P]
    //     */
    //    implicit def entities[L <: Rule, R <: Rule, E <: Rule.Entity, I, O, P](
    //        implicit
    //        lr: Resolver.Aux[L, E],
    //        lv: Validation[L, I, O],
    //        rr: Resolver.Aux[R, E],
    //        rv: Validation[R, O, P]
    //    ): Resolver.Aux[L && R, E] = Resolver.instance

    /**
     * condition[I] && transition[I, O] -> mutation[I, O]
     */
    implicit def conditionTransition[L <: Rule, R <: Rule, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Condition],
        lv: Validation[L, I, I],
        rr: Resolver.Aux[R, _ <: Rule.Transition],
        rv: Validation[R, I, O]
    ): Resolver.Aux[L && R, Rule.Mutation] = Resolver.instance

    /**
     * transition[I, O] && condition[O] -> mutation[I, O]
     */
    implicit def transformationCondition[L <: Rule, R <: Rule, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Transition],
        lv: Validation[L, I, O],
        rr: Resolver.Aux[R, _ <: Rule.Condition],
        rv: Validation[R, O, O]
    ): Resolver.Aux[L && R, Rule.Mutation] = Resolver.instance

    /**
     * condition[I] && mutation[I, O] -> mutation[I, O]
     */
    implicit def conditionMutation[L <: Rule, R <: Rule, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Condition],
        lv: Validation[L, I, I],
        rr: Resolver.Aux[R, _ <: Rule.Mutation],
        rv: Validation[R, I, O]
    ): Resolver.Aux[L && R, Rule.Mutation] = Resolver.instance

    /**
     * mutation[I, O] && condition[O] -> mutation[I, O]
     */
    implicit def mutationCondition[L <: Rule, R <: Rule, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Mutation],
        lv: Validation[L, I, O],
        rr: Resolver.Aux[R, _ <: Rule.Condition],
        rv: Validation[R, O, O]
    ): Resolver.Aux[L && R, Rule.Mutation] = Resolver.instance
}

trait AndResolvers0 {
    implicit def conditions[L <: Rule, R <: Rule, T](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Condition],
        lv: Validation[L, T, T],
        rr: Resolver.Aux[R, _ <: Rule.Condition],
        rv: Validation[R, T, T]
    ): Resolver.Aux[L && R, Rule.Condition] = Resolver.instance

    implicit def mutations[L <: Rule, R <: Rule, I, O, P](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Mutation],
        lv: Validation[L, I, O],
        rr: Resolver.Aux[R, _ <: Rule.Mutation],
        rv: Validation[R, O, P]
    ): Resolver.Aux[L && R, Rule.Mutation] = Resolver.instance

    implicit def transitions[L <: Rule, R <: Rule, I, O, P](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Transition],
        lv: Validation[L, I, O],
        rr: Resolver.Aux[R, _ <: Rule.Transition],
        rv: Validation[R, O, P]
    ): Resolver.Aux[L && R, Rule.Transition] = Resolver.instance
}