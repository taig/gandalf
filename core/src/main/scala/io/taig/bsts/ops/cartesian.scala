package io.taig.bsts.ops

import cats.data.{ Validated, NonEmptyList }
import cats.std.list._
import cats.syntax.cartesian._

class cartesian[O]( r1: Validated[NonEmptyList[( String, List[Any] )], O] ) {
    def |@|[P]( r2: Validated[NonEmptyList[( String, List[Any] )], P] ) = r1 |@| r2
}