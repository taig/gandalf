package io.taig.gandalf.core

import scala.language.higherKinds

class not[R <: Rule] extends Rule

object not extends not0 {
    @inline
    def apply[R <: Rule]( rule: R )(
        implicit
        r: Resolver[not[R]]
    ): not[R] = new not[R]

    implicit def resolverCondition[R <: Rule.Condition]: Resolver.Aux[not[R], R] =
        Resolver.instance

    implicit def validation[R <: Rule, T](
        implicit
        r: Resolver.Aux[R, _ <: Rule.Condition],
        v: Validation[R, T, T]
    ): Validation[not[R], T, T] = Validation.instance { input ⇒
        v( input ) match {
            case Some( _ ) ⇒ None
            case None      ⇒ Some( input )
        }
    }

    implicit def validationConditionTransformation[L <: Rule, R <: Rule, OP[_ <: Rule, _ <: Rule] <: Rule.Operator, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Condition],
        rr: Resolver.Aux[R, _ <: Rule.Transformation],
        v:  Validation[not[L] OP R, I, O]
    ): Validation[not[L OP R], I, O] = Validation.instance( v( _ ) )

    implicit def validationTransformationCondition[L <: Rule, R <: Rule, OP[_ <: Rule, _ <: Rule] <: Rule.Operator, I, O](
        implicit
        lr: Resolver.Aux[L, _ <: Rule.Transformation],
        rr: Resolver.Aux[R, _ <: Rule.Condition],
        v:  Validation[L OP not[R], I, O]
    ): Validation[not[L OP R], I, O] = Validation.instance( v( _ ) )

    implicit def serialization[R <: Rule](
        implicit
        s: Serialization[R]
    ): Serialization[not[R]] = Serialization.instance( s"not($s)" )
}

trait not0 {
    implicit def resolver[R <: Rule, I, O](
        implicit
        r: Resolver[R],
        v: Validation[not[R], I, O]
    ): Resolver.Aux[not[R], r.Out] = Resolver.instance
}