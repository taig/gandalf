package io.taig.bsts.ops

import cats.data.{ NonEmptyList, Validated }
import io.taig.bsts.{ Error, Raw }
import shapeless.HList

class rawError[N <: String, A <: HList]( error: Error[N, A] )( implicit r: Raw[Error[N, A]] ) {
    def raw: NonEmptyList[( String, List[Any] )] = r.raw( error )
}

class rawValidated[E, A]( validated: Validated[E, A] )( implicit r: Raw[E] ) {
    def raw: Validated[NonEmptyList[( String, List[Any] )], A] = validated.leftMap( r.raw )
}