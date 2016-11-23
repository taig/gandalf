package io.taig.gandalf.core

import io.taig.gandalf.core.Validation._

class And[L <: Rule, R <: Rule] extends Rule.Operator[L, R]

object And extends And0 {
    type Aux[L <: Rule, R <: Rule, Out0 <: Rule] = And[L, R] { type Out = Out0 }

    //    /**
    //     * condition && condition
    //     */
    //    implicit def conditions[L, R, T](
    //        implicit
    //        l: Condition[L, T],
    //        r: Condition[R, T]
    //    ): Condition[L && R, T] = Condition.instance { input ⇒
    //        l.check( input ) && r.check( input )
    //    }
    //
    //    /**
    //     * transformation && transformation
    //     */
    //    implicit def transformations[L, R, I, O, P](
    //        implicit
    //        l: Transformation[L, I, O],
    //        r: Transformation[R, O, P]
    //    ): Transformation[L && R, I, P] = Transformation.instance {
    //        l.transform _ andThen r.transform
    //    }
    //
    //    /**
    //     * not( condition && mutation )
    //     */
    //    implicit def notConditionMutation[L, R, I, O](
    //        implicit
    //        l: Condition[not[L], I],
    //        r: Mutation[R, I, O],
    //        v: Validation[not[L] && R, I, O]
    //    ): Mutation[not[L && R], I, O] = Mutation.instance( v.apply )
    //
    //    /**
    //     * not( condition && transformation )
    //     */
    //    implicit def notConditionTransformation[L, R, I, O](
    //        implicit
    //        l: Condition[not[L], I],
    //        r: Transformation[R, I, O],
    //        v: Validation[not[L] && R, I, O]
    //    ): Mutation[not[L && R], I, O] = Mutation.instance( v.apply )
    //
    //    /**
    //     * not( mutation && condition )
    //     */
    //    implicit def notMutationCondition[L, R, I, O](
    //        implicit
    //        l: Mutation[L, I, O],
    //        r: Condition[not[R], O],
    //        v: Validation[L && not[R], I, O]
    //    ): Mutation[not[L && R], I, O] = Mutation.instance( v.apply )
    //
    //    /**
    //     * not( transformation && condition )
    //     */
    //    implicit def notTransformationCondition[L, R, I, O](
    //        implicit
    //        l: Transformation[L, I, O],
    //        r: Condition[not[R], O],
    //        v: Validation[L && not[R], I, O]
    //    ): Mutation[not[L && R], I, O] = Mutation.instance( v.apply )

    implicit def serialization[L <: Rule, R <: Rule](
        implicit
        l: Serialization[L],
        r: Serialization[R]
    ): Serialization[L && R] = {
        Serialization.instance( s"(${l.serialize} && ${r.serialize})" )
    }
}

trait And0 {
    //    implicit def validation[L, R, I, O, P](
    //        implicit
    //        l: Validation[L, I, O],
    //        r: Validation[R, O, P]
    //    ): Mutation[L && R, I, P] = Mutation.instance { input ⇒
    //        l( input ) flatMap r.apply
    //    }
}