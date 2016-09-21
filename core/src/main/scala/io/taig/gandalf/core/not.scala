package io.taig.gandalf.core

class not[R <: Rule]( implicit n: Negation[R] ) extends Rule {
    override type Input = R#Input

    override type Output = R#Output

    override type Arguments = R#Arguments
}

object not {
    def apply[R <: Rule: Negation]( Rule: R ): not[R] = new not[R]

    implicit def validation[R <: Rule](
        implicit
        n: Negation[R]
    ): Validation[not[R]] = Validation.instance[not[R]]( n.negate )

    implicit def error[R <: Rule](
        implicit
        e: Error[R]
    ): Error[not[R]] = Error.instance[not[R]] { arguments ⇒
        e.show( arguments ).map( error ⇒ s"not($error)" )
    }

    implicit def arguments[R <: Rule](
        implicit
        a: Arguments[R]
    ): Arguments[not[R]] = Arguments.instance[not[R]]( a.collect )
}