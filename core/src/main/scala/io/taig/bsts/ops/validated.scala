package io.taig.bsts.ops

import cats.data.Validated
import cats.data.NonEmptyList
import io.taig.bsts.{ Raw, Error }
import io.taig.bsts.implicits._
import shapeless.HList

class validated[N <: String, O, A <: HList]( a: Validated[Error[N, A], O] )(
        implicit
        r1: Raw[Validated[Error[N, A], O], Validated[( String, List[Any] ), O]]
) {
    def |@|[M <: String, P, B <: HList]( b: Validated[Error[M, B], P] )(
        implicit
        r2: Raw[Validated[Error[M, B], P], Validated[( String, List[Any] ), P]]
    ) = {
        import cats.std.all._
        import cats.syntax.cartesian._
        a.raw.leftMap( NonEmptyList( _ ) ) |@| b.raw.leftMap( NonEmptyList( _ ) )
    }
}
