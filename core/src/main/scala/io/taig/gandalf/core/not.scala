package io.taig.gandalf.core

import io.taig.gandalf.core.Validation._

class not[+R <: Rule] extends Rule {
    override type Out = this.type
}

object not {
    @inline
    def apply[R]( validation: R ): not[validation.type] = {
        new not[validation.type]
    }

    implicit def condition[C, T](
        implicit
        c: Condition[C, T]
    ): Condition[not[C], T] = Condition.instance( !c.check( _ ) )
}