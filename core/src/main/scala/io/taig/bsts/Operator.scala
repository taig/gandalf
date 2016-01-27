package io.taig.bsts

/**
 * Policy operators
 */
sealed abstract class Operator( name: String ) {
    override def toString = name
}

object Operator {
    //    sealed abstract class Unary( name: String ) extends Operator( name )
    //    case object ¬ extends Unary( "¬" )

    sealed abstract class Binary( name: String ) extends Operator( name )
    case object & extends Binary( "&" )
    case object && extends Binary( "&&" )
    case object | extends Binary( "|" )
    case object || extends Binary( "||" )
    case object ^ extends Binary( "^" )
}