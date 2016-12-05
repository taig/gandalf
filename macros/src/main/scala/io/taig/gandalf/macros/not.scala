//package io.taig.gandalf.macros
//
//import io.taig.gandalf.core.{ Rule, Serialization }
//
//class not[R <: Rule] extends Rule
//
//object not {
//    implicit def resolver[R <: Rule: Serialization, O <: Rule](
//        implicit
//        r: Resolver.Aux[R, O],
//        s: Serialization[O]
//    ): Resolver.Aux[not[R], O] = Resolver.instance
//
//    implicit def serialization[R <: Rule](
//        implicit
//        s: Serialization[R]
//    ): Serialization[not[R]] = Serialization.instance( s"not($s)" )
//}