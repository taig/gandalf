//package io.taig.gandalf.predef
//
//import io.taig.gandalf.data.Rule
//import io.taig.gandalf.{ Error, Validation }
//import shapeless._
//
//import scala.language.existentials
//
////class Matches[T, I >: T] extends Rule {
////    override type Input = I
////
////    override type Arguments = Error.Expectation[Matches[T, I]]
////}
////
////object Matches {
////    implicit def validation[T, I >: T](
////        implicit
////        w: Witness.Aux[T],
////        e: Error[Matches[T, I]]
////    ) = Validation.rule[I, Matches[T, I]]( _ == w.value )( Error.expectation[Matches[T, I]]( _, w.value ) )
////
////    def matches[T, I >: T]( compare: Witness.Aux[T] ): Matches[T, I] = new Matches[T, I]
////}