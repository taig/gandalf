package io.taig.gandalf.core

class Or[L <: Rule, R <: Rule] extends Rule.Operator

object Or extends OrResolvers {
    implicit def validation[L <: Rule, R <: Rule, I, O](
        implicit
        l:  Validation[L, I, O],
        r:  Validation[R, I, O],
        rs: Resolver[L || R]
    ): Validation[L || R, I, O] = Validation.instance { input ⇒
        l( input ) match {
            case Some( output ) ⇒ Some( output )
            case None ⇒ r( input ) match {
                case Some( output ) ⇒ Some( output )
                case None           ⇒ None
            }
        }
    }

    implicit def serialization[L <: Rule, R <: Rule](
        implicit
        l: Serialization[L],
        r: Serialization[R]
    ): Serialization[L || R] = Serialization.instance( s"($l || $r)" )
}

trait OrResolvers {
    /**
     * condition[T] || condition[T] -> condition[T]
     */
    implicit def conditions[L <: Rule.Condition, R <: Rule.Condition, T](
        implicit
        l: Validation[L, T, T],
        r: Validation[R, T, T]
    ): Resolver.Aux[L || R, Rule.Condition] = Resolver.instance

    /**
     * mutation[I, O] || mutation[I, O] -> mutation[I, O]
     */
    implicit def mutations[L <: Rule.Mutation, R <: Rule.Mutation, I, O](
        implicit
        l: Validation[L, I, O],
        r: Validation[R, I, O]
    ): Resolver.Aux[L || R, Rule.Mutation] = Resolver.instance
}