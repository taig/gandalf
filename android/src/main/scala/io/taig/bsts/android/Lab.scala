package io.taig.bsts.android

import io.taig.bsts.android.implicits._
import io.taig.bsts.rule.all._
import cats.syntax.cartesian._
import cats.std.list._

object Lab extends App {
    required.validate( "yolo" ) |@| min( 8 ).validate( "yolo" )
}
