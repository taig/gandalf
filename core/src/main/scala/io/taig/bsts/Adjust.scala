package io.taig.bsts

/**
 * Type class that adjusts a value before validation (e.g. trimming a String)
 */
trait Adjust[R <: Rule] {
    def adjust( value: R#Value ): R#Value
}

object Adjust {
    def apply[R <: Rule]( f: R#Value ⇒ R#Value ): Adjust[R] = new Adjust[R] {
        override def adjust( value: R#Value ) = f( value )
    }

    implicit def `Adjust[Rule]`[R <: Rule] = Adjust[R]( identity )

    implicit def `Adjust[Rule[String]]`[R <: Rule.Aux[String]] = Adjust[R]( _.trim )
}