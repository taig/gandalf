package io.taig.gandalf.playground

import io.taig.gandalf.core.implicits._
import io.taig.gandalf.core.{ &&, not, _ }
import io.taig.gandalf.predef.string._

object Playground extends App {
    //    not( required ).validate( "" )
    //    object required extends ( trim.type && not[isEmpty.type] )
    println( not( trim && not( isEmpty ) ).validate( "" ) )
    ( required && isEmpty ).validate( "" )
}