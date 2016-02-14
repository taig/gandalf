package io.taig.bsts.ops.dsl

sealed abstract class Operator( name: String ) {
    override def toString = name
}

object Operator {
    sealed abstract class Binary( name: String ) extends Operator( name )

    case object & extends Binary( "&" )
    case object && extends Binary( "&&" )
    case object | extends Binary( "|" )
    case object || extends Binary( "||" )
    case object ^ extends Binary( "^" )
    case object ~> extends Binary( "~>" )
}