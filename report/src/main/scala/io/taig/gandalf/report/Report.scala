package io.taig.gandalf.report

import cats.data.NonEmptyList

import scala.language.existentials

trait Report[-V, I] {
    def show( input: I ): NonEmptyList[String]
}

object Report {
    @inline
    def apply[V, I]( implicit r: Report[V, I] ): Report[V, I] = r

    def instance[V, I]( f: I ⇒ NonEmptyList[String] ): Report[V, I] = {
        new Report[V, I] {
            override def show( input: I ): NonEmptyList[String] = f( input )
        }
    }

    def single[V, I]( f: I ⇒ String ): Report[V, I] = {
        instance( f.andThen( NonEmptyList.of( _ ) ) )
    }

    def static[V](
        message: String
    ): Report[V, I forSome { type I }] = single( _ ⇒ message )
}