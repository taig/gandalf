package io.taig.gandalf.predef

import io.taig.gandalf._

import scala.language.higherKinds

abstract class NumericComparison[R[_] <: Rule.Condition](
        f: ( Double, Double ) ⇒ Boolean
) {
    @inline
    private def lhs[U: Numeric]( input: U ): Double =
        implicitly[Numeric[U]].toDouble( input )

    private def instance[T, U: Numeric]( value: Double ): Validation[R[T], U, U] =
        Validation.condition( input ⇒ f( lhs( input ), value ) )

    implicit def byte[T <: Byte: ValueOf, U: Numeric]: Validation[R[T], U, U] =
        instance( valueOf[T].toDouble )

    implicit def double[T <: Double: ValueOf, U: Numeric]: Validation[R[T], U, U] =
        instance( valueOf[T] )

    implicit def float[T <: Float: ValueOf, U: Numeric]: Validation[R[T], U, U] =
        instance( valueOf[T].toDouble )

    implicit def int[T <: Int: ValueOf, U: Numeric]: Validation[R[T], U, U] =
        instance( valueOf[T].toDouble )

    implicit def long[T <: Long: ValueOf, U: Numeric]: Validation[R[T], U, U] =
        instance( valueOf[T].toDouble )

    implicit def short[T <: Short: ValueOf, U: Numeric]: Validation[R[T], U, U] =
        instance( valueOf[T].toDouble )
}