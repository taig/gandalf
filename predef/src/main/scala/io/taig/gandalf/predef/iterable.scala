package io.taig.gandalf.predef

import io.taig.gandalf._

import scala.language.higherKinds

trait iterable {
    @Definition
    sealed class isEmpty[T] extends Rule with Input[Iterable[T]] with Arguments.Input {
        override def check( input: Iterable[T] ) = input.isEmpty
    }

    @Definition
    sealed class nonEmpty[T] extends Rule with Input[Iterable[T]] with Arguments.Input {
        override def check( input: Iterable[T] ) = input.nonEmpty
    }
}

object iterable extends iterable