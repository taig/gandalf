package io.taig.gandalf.predef

import io.taig.gandalf.Rule
import shapeless._
import shapeless.syntax.singleton._

import scala.Ordering.Implicits._

trait numeric {
    def eq[N: Numeric]( size: N ) = Rule[N]( "eq" )( _ equiv size ) { "value" ->> _ :: HNil }

    def gt[N: Numeric]( size: N ) = Rule[N]( "gt" )( _ > size ) { "value" ->> _ :: HNil }

    def gte[N: Numeric]( size: N ) = Rule[N]( "gte" )( _ >= size ) { "value" ->> _ :: HNil }

    def lt[N: Numeric]( size: N ) = Rule[N]( "lt" )( _ < size ) { "value" ->> _ :: HNil }

    def lte[N: Numeric]( size: N ) = Rule[N]( "lte" )( _ <= size ) { "value" ->> _ :: HNil }
}

object numeric extends numeric