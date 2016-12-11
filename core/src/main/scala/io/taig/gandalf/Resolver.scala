package io.taig.gandalf

trait Resolver[-R <: Rule] {
    type Out <: Rule

    private[gandalf] def rule: Serialization[R]

    private[gandalf] def out: Serialization[Out]

    override def equals( obj: Any ): Boolean = obj match {
        case r: Resolver[_] ⇒ this.rule == r.rule && this.out == r.out
        case _              ⇒ false
    }

    override def toString = s"Resolver($rule ~> $out)"
}

object Resolver {
    type Aux[R <: Rule, O <: Rule] = Resolver[R] {
        type Out = O
    }

    @inline
    def apply[R <: Rule: Serialization, O <: Rule: Serialization](
        implicit
        r: Resolver.Aux[R, O]
    ): Aux[R, O] = r

    def instance[R <: Rule, O <: Rule](
        implicit
        r: Serialization[R],
        o: Serialization[O]
    ): Aux[R, O] = {
        new Resolver[R] {
            override type Out = O

            override val rule = r

            override val out = o
        }
    }

    implicit def condition[C <: Rule.Condition: Serialization]: Resolver.Aux[C, Rule.Condition] =
        instance[C, Rule.Condition]

    implicit def mutation[M <: Rule.Mutation: Serialization]: Resolver.Aux[M, Rule.Mutation] =
        instance[M, Rule.Mutation]

    implicit def transition[T <: Rule.Transition: Serialization]: Resolver.Aux[T, Rule.Transition] =
        instance[T, Rule.Transition]
}