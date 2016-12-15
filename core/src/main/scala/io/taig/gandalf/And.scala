package io.taig.gandalf

class And[L <: Rule, R <: Rule] extends Operator

object And extends Operator.Implicits[And, Rule.Entity, Rule.Transformation] {
    override val symbol = "&&"

    implicit def validation[L <: Rule, R <: Rule, I, O, P](
        implicit
        l: Validation[L, I, O],
        r: Validation[R, O, P]
    ): Validation[L && R, I, P] =
        Validation.instance[L && R, I, P]( l.confirm( _ ) flatMap r.confirm )
}