package io.taig.gandalf.circe.test

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.taig.gandalf.macros._
import io.taig.gandalf.circe._
import io.taig.gandalf.core.Error
import io.taig.gandalf.macros.implicits._
import io.taig.gandalf.predef.string.required

object Playground {
    def main( args: Array[String] ): Unit = {
        implicit val error: Error[required.type] = Error.static( "Pflichtfeld" )

        val otto = User( "Otto" )

        println( otto.asJson.noSpaces )

        val json1 = """{ "name" : "Otto" }"""

        println( decode[User]( json1 ) )

        val json2 = """{ "name" : "" }"""

        println( decode[User]( json2 ) )
    }
}

//case class Test( @MyAnnotation name: String )

//object Test {
//    def foo( @obeys2( required ) bar:String ) = ???
//}