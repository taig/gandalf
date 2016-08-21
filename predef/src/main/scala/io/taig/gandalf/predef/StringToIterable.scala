//package io.taig.gandalf.predef
//
//import io.taig.gandalf.Validation
//import io.taig.gandalf.data.Transformation
//
//class StringToIterable extends Transformation {
//    override type Input = String
//
//    override type Output = Iterable[Char]
//}
//
//object StringToIterable {
//    implicit val validation: Validation[Iterable[Char], StringToIterable] = Validation.transformation( _.to[List] )
//
//    val stringToIterable: StringToIterable = new StringToIterable
//}