//package io.taig.gandalf.predef
//
//import io.taig.gandalf.data.Rule
//import io.taig.gandalf.{ Error, Validation }
//
//final class NonEmpty extends Rule {
//    override type Input = Iterable[_]
//
//    override type Arguments = Error.Input[NonEmpty]
//}
//
//object NonEmpty {
//    implicit def validation(
//        implicit
//        e: Error[NonEmpty]
//    ): Validation[Iterable[_], NonEmpty] = {
//        Validation.rule[Iterable[_], NonEmpty]( _.nonEmpty )( Error.input[NonEmpty] )
//    }
//
//    val nonEmpty: NonEmpty = new NonEmpty
//}