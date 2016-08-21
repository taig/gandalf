package io.taig.gandalf

trait LazyAnd extends Operator with Rule {
    override type Left <: Rule

    override type Right <: Rule.Aux[Left#Output]
}

object LazyAnd {
    type Aux[L <: Rule, R <: Rule.Aux[L#Output]] = LazyAnd {
        type Left = L

        type Right = R
    }

    implicit def validation[LA <: LazyAnd](
        implicit
        l: Validation[LA#Left],
        r: Validation[LA#Right],
        e: Error[LA]
    ): Validation[LA] = Validation.operation[LA] {
        l.validate( _ ).andThen { output ⇒
            r.validate( output.asInstanceOf[LA#Right#Input] )
        }
    } { ( _, _ ) }
}