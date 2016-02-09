package io.taig.bsts.ops.dsl

sealed abstract class Operator( name: String ) {
    override def toString = name
}

object Operator {
    sealed trait Rule
    sealed trait Transformation

    sealed abstract class Binary( name: String ) extends Operator( name )

    case object & extends Binary( "&" ) with Rule
    case object && extends Binary( "&&" ) with Rule
    case object | extends Binary( "|" ) with Rule
    case object || extends Binary( "||" ) with Rule
    case object ^ extends Binary( "^" ) with Rule

    case object ~> extends Binary( "~>" ) with Transformation

    sealed abstract class Unary( name: String ) extends Operator( name )

    case object ! extends Unary( "!" ) with Rule
}