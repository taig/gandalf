package io.taig.gandalf.report

import cats.data.NonEmptyList
import io.taig.gandalf.core.Rule

import scala.language.existentials

trait Report[-R <: Rule, I] {
    def show( input: I ): NonEmptyList[String]
}

object Report {
    @inline
    def apply[R <: Rule, I]( implicit r: Report[R, I] ): Report[R, I] = r

    def instance[R <: Rule, I]( f: I ⇒ NonEmptyList[String] ): Report[R, I] = {
        new Report[R, I] {
            override def show( input: I ): NonEmptyList[String] = f( input )
        }
    }

    def single[R <: Rule, I]( f: I ⇒ String ): Report[R, I] = {
        instance( f.andThen( NonEmptyList.of( _ ) ) )
    }

    def static[R <: Rule](
        message: String
    ): Report[R, I forSome { type I }] = single( _ ⇒ message )
}