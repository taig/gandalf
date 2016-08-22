//package io.taig.gandalf
//
//import cats.std.list._
//import cats.syntax.cartesian._
//
//trait EagerAnd extends Operator with Rule {
//    override type Left <: Rule
//
//    override type Right <: Rule.Aux[Left#Output]
//}
//
//object EagerAnd {
//    type Aux[L <: Rule, R <: Rule.Aux[L#Output]] = EagerAnd {
//        type Left = L
//
//        type Right = R
//    }
//
//    implicit def validation[LA <: EagerAnd](
//        implicit
//        l: Validation[LA#Left],
//        r: Validation[LA#Right],
//        e: Error[LA]
//    ): Validation[LA] = Validation.operation[LA] { input ⇒
//        val left = l.validate( input )
//        val right = r.validate( input.asInstanceOf[LA#Right#Input] )
//        ( left |@| right ) map { case ( _, _ ) ⇒ input }
//    } { ( _, _ ) }
//}