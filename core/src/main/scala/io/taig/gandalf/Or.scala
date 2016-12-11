package io.taig.gandalf

class Or[L <: Rule, R <: Rule] extends Operator

object Or extends Operator.Implicits[Or, Rule.Unsafe, Rule.Mutation] {
    override val symbol = "||"

    implicit def validation[L <: Rule, R <: Rule, I, O](
        implicit
        l:  Validation[L, I, O],
        r:  Validation[R, I, O],
        rs: Resolver[L || R]
    ): Validation[L || R, I, O] = Validation.instance[L || R, I, O] { input ⇒
        l.confirm( input ) orElse r.confirm( input )
    }
}