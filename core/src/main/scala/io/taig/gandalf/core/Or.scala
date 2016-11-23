package io.taig.gandalf.core

import io.taig.gandalf.core.Validation._

class Or[L, R]

object Or {
    implicit def conditions[L, R, T](
        implicit
        l: Condition[L, T],
        r: Condition[R, T]
    ): Condition[L || R, T] = Condition.instance { input ⇒
        l.check( input ) || r.check( input )
    }

    implicit def conditionMutation[L, R, T](
        implicit
        l: Condition[L, T],
        r: Mutation[R, T, T]
    ): Mutation[L || R, T, T] = Mutation.instance { input ⇒
        l( input ) orElse r.mutate( input )
    }

    implicit def mutations[L, R, I, O](
        implicit
        l: Mutation[L, I, O],
        r: Mutation[R, I, O]
    ): Mutation[L || R, I, O] = Mutation.instance { input ⇒
        l( input ) orElse r( input )
    }

    implicit def mutationCondition[L, R, T](
        implicit
        l: Mutation[L, T, T],
        r: Condition[R, T]
    ): Mutation[L || R, T, T] = Mutation.instance { input ⇒
        l( input ) orElse r( input )
    }

    /**
     * not( condition && mutation )
     */
    implicit def notConditionMutation[L, R, T](
        implicit
        l: Condition[not[L], T],
        r: Mutation[R, T, T],
        v: Validation[not[L] || R, T, T]
    ): Mutation[not[L || R], T, T] = Mutation.instance( v.apply )

    /**
     * not( mutation && condition )
     */
    implicit def notMutationCondition[L, R, T](
        implicit
        l: Mutation[L, T, T],
        r: Condition[not[R], T],
        v: Validation[L || not[R], T, T]
    ): Mutation[not[L || R], T, T] = Mutation.instance( v.apply )

    implicit def serialization[L, R](
        implicit
        l: Serialization[L],
        r: Serialization[R]
    ): Serialization[L || R] = {
        Serialization.instance( s"(${l.serialize} || ${r.serialize})" )
    }
}