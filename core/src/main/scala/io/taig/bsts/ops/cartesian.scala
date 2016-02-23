package io.taig.bsts.ops

import cats.data.{ Validated, NonEmptyList }
import cats.std.list._
import cats.syntax.cartesian._

class cartesian[A]( r1: Validated[NonEmptyList[( String, List[Any] )], A] ) {
    def |@|[B]( r2: Validated[NonEmptyList[( String, List[Any] )], B] ) = r1 |@| r2
}