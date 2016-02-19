package io.taig.bsts.ops

import cats.data.{ Validated, NonEmptyList }
import cats.syntax.cartesian._
import cats.std.list._

class cartesian[E, O]( r1: Validated[NonEmptyList[E], O] ) {
    def |@|[P]( r2: Validated[NonEmptyList[E], P] ) = r1 |@| r2
}