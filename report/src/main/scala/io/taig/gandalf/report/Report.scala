package io.taig.gandalf.report

import cats.data.NonEmptyList
import io.taig.gandalf.core.Rule

import scala.language.existentials

trait Report[-R <: Rule, I, O] {
    def show( input: I ): NonEmptyList[String]
}

object Report {
    @inline
    def apply[R <: Rule, I, O](
        implicit
        r: Report[R, I, O]
    ): Report[R, I, O] = r

    def instance[R <: Rule, I, O](
        f: I ⇒ NonEmptyList[String]
    ): Report[R, I, O] = new Report[R, I, O] {
        override def show( input: I ): NonEmptyList[String] = f( input )
    }

    def single[R <: Rule, I, O]( f: I ⇒ String ): Report[R, I, O] = {
        instance( f.andThen( NonEmptyList.of( _ ) ) )
    }

    def static[R <: Rule, I, O](
        message: String
    ): Report[R, I, O] = single[R, I, O]( _ ⇒ message )
}