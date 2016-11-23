package io.taig.gandalf.core

import io.taig.gandalf.core.Validation._

class not[R <: Rule] extends Rule {
    override final type Out = R#Out
}

object not {
    @inline
    def apply( rule: Rule ): not[rule.type] = new not[rule.type]

    implicit def condition[C <: Rule.Entity.Condition, T](
        implicit
        c: Condition[C, T]
    ): Condition[not[C], T] = Condition.instance( !c.check( _ ) )
}