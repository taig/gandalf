package io.taig.bsts.ops.dsl

import io.taig.bsts._

import scala.language.experimental.macros

final class transformation[I, O, V <: Validation[I, O]]( a: V ) {
    @operator( Operator.~> ) def ~>[P]( b: Validation[O, P] ): Validation[I, P] = macro impl[V, I, O, P]
}