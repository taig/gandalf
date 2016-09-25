package io.taig.gandalf.playground

import io.taig.gandalf.core.implicits._
import io.taig.gandalf.core._
import io.taig.gandalf.predef.string._

object Playground extends App {
    //    not( required ).validate( "" )
    //    println( not( trim && not( isEmpty ) ).validate( "" ) )
    val foobar = required && matches( "foobar" )
    foobar.validate( "" )
}