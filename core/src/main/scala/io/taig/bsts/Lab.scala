package io.taig.bsts

import scala.language.implicitConversions

object Lab extends App {
    val w1 = "r1"
    val r1 = Rule.empty[String]( w1 )( _ ⇒ true )
    val w2 = "r2"
    val r2 = Rule.empty[String]( w2 )( _ ⇒ false )
    val w3 = "r3"
    val r3 = Rule.empty[String]( w3 )( _ ⇒ true )
    val w4 = "r4"
    val r4 = Rule.empty[String]( w4 )( _ ⇒ false )

    println( Policy( r1 ).validate( "asdf" ) )
    println( ( r1 & r2 ).validate( "asdf" ) )
    println( ( ( r2 & r1 ) & ( r1 & r2 ) ).validate( "asdf" ) )
    println( ( ( r4 & r2 ) & ( r2 & r4 ) ).validate( "asdf" ) )
    println( ( r1 && r2 ).validate( "asdf" ) )
    println( ( r1 && r3 ).validate( "asdf" ) )
}