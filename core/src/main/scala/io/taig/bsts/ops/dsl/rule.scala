package io.taig.bsts.ops.dsl

import io.taig.bsts._

import scala.language.experimental.macros

final class rule[T, A <: Validation[T, T]]( a: A ) {
    @operator( Operator.& ) def &( b: Validation[T, T] ): Validation[T, T] = macro impl[A, T, T, T]

    @operator( Operator.&& ) def &&( b: Validation[T, T] ): Validation[T, T] = macro impl[A, T, T, T]

    @operator( Operator.| ) def |( b: Validation[T, T] ): Validation[T, T] = macro impl[A, T, T, T]

    @operator( Operator.|| ) def ||( b: Validation[T, T] ): Validation[T, T] = macro impl[A, T, T, T]

    @operator( Operator.^ ) def ^( b: Validation[T, T] ): Validation[T, T] = macro impl[A, T, T, T]
}