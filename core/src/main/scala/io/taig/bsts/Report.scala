package io.taig.bsts

import shapeless._
import shapeless.ops.function.FnToProduct

trait Report[I <: String, A <: HList] {
    def report( error: Error[I, A] ): String
}

object Report {
    def apply[I <: String, A <: HList]( f: A ⇒ String ): Report[I, A] = new Report[I, A] {
        override def report( error: Error[I, A] ): String = f( error.arguments )
    }

    def apply[I <: String, A <: HList]( rule: Rule[I, _, A] )( message: String ): Report[I, A] = Report( _ ⇒ message )

    def apply[I <: String, A <: HList, F, L, R]( rule: F )(
        implicit
        ftp: FnToProduct.Aux[F, L ⇒ R],
        ev1: R <:< Rule[I, _, A]
    ): Builder[I, A] = new Builder

    class Builder[I <: String, A <: HList] {
        def as( f: A ⇒ String ): Report[I, A] = Report( f )
    }
}