package io.taig.bsts.rule

import shapeless._
import shapeless.syntax.singleton._

trait string {
    val email = {
        val pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".r.pattern

        Rule[String]( "email" )( pattern.matcher( _ ).matches() ){ "value" ->> _ :: HNil }
    }

    def exactly( length: Int ) = Rule[String, Int]( "exactly" )( _.length )( _ == length ) { ( value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    def max( length: Int ) = Rule[String, Int]( "max" )( _.length )( _ <= length ) { ( value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    val phone = {
        val pattern = "^\\+?0{0,2}[1-9]\\d{3,}$".r.pattern

        Rule[String]( "phone" )( pattern.matcher( _ ).matches() ){ "value" ->> _ :: HNil }
    }
}

object string extends string