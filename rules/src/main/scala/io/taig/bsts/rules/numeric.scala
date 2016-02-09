package io.taig.bsts.rules

import io.taig.bsts.Validation$
import shapeless._
import shapeless.syntax.singleton._

import scala.Ordering.Implicits._

trait numeric {
    def eq[N: Numeric]( size: N ) = Validation[N]( "eq" )( _ equiv size ) { "value" ->> _ :: HNil }

    def gt[N: Numeric]( size: N ) = Validation[N]( "gt" )( _ > size ) { "value" ->> _ :: HNil }

    def gte[N: Numeric]( size: N ) = Validation[N]( "gte" )( _ >= size ) { "value" ->> _ :: HNil }

    def lt[N: Numeric]( size: N ) = Validation[N]( "lt" )( _ < size ) { "value" ->> _ :: HNil }

    def lte[N: Numeric]( size: N ) = Validation[N]( "lte" )( _ <= size ) { "value" ->> _ :: HNil }
}

object numeric extends numeric