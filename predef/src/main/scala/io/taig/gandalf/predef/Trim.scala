//package io.taig.gandalf.predef
//
//import io.taig.gandalf.Validation
//import io.taig.gandalf.data.Transformation
//
//class Trim extends Transformation {
//    override type Input = String
//
//    override type Output = String
//}
//
//object Trim {
//    implicit val validation: Validation[String, Trim] = Validation.transformation( _.trim )
//
//    val trim: Trim = new Trim
//}