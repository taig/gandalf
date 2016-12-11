package io.taig.gandalf.core

import scala.language.higherKinds

class not[R <: Rule] extends Rule

object not {
    def apply[R <: Rule]( rule: R ): not[R] = new not[R]

    implicit def validation[R <: Rule, T](
        implicit
        r: Resolver.Aux[R, Rule.Condition],
        v: Validation.Aux[R, T, T]
    ): Validation.Aux[not[R], T, T] = Validation.instance[not[R], T, T] { input ⇒
        v.confirm( input ).fold[Option[T]]( Some( input ) )( _ ⇒ None )
    }

    implicit def condition[R <: Rule: Serialization](
        implicit
        r: Resolver.Aux[R, Rule.Condition]
    ): Resolver.Aux[not[R], Rule.Condition] =
        Resolver.instance[not[R], Rule.Condition]

    implicit def conditionTransformation[L <: Rule, R <: Rule, O <: Rule, OP[_ <: Rule, _ <: Rule] <: Operator](
        implicit
        l:  Resolver.Aux[L, Rule.Condition],
        r:  Resolver.Aux[R, _ <: Rule.Transformation],
        rs: Resolver.Aux[not[L] OP R, O],
        sr: Serialization[L OP R],
        so: Serialization[O]
    ): Resolver.Aux[not[L OP R], O] = Resolver.instance[not[L OP R], O]

    implicit def transformationCondition[L <: Rule, R <: Rule, O <: Rule, OP[_ <: Rule, _ <: Rule] <: Operator](
        implicit
        l:  Resolver.Aux[L, _ <: Rule.Transformation],
        r:  Resolver.Aux[R, Rule.Condition],
        rs: Resolver.Aux[L OP not[R], O],
        sr: Serialization[L OP R],
        so: Serialization[O]
    ): Resolver.Aux[not[L OP R], O] = Resolver.instance[not[L OP R], O]

    implicit def serialization[R <: Rule](
        implicit
        s: Serialization[R]
    ): Serialization[not[R]] = Serialization.instance[not[R]]( s"not($s)" )
}