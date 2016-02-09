package io.taig.bsts.rules

import io.taig.bsts.Validation$
import shapeless._
import shapeless.syntax.singleton._

trait string {
    val email = {
        val pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".r.pattern

        Validation[String]( "email" )( pattern.matcher( _ ).matches() ){ "value" ->> _ :: HNil }
    }

    def exactly( length: Int ) = Validation[String, Int]( "exactly" )( _.length )( _ == length ) { (value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    def max( length: Int ) = Validation[String, Int]( "max" )( _.length )( _ <= length ) { (value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    def min( length: Int ) = Validation[String, Int]( "min" )( _.length )( _ >= length ) { (value, actual ) ⇒
        "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
    }

    val phone = {
        val pattern = "^\\+?0{0,2}[1-9]\\d{3,}$".r.pattern

        Validation[String]( "phone" )( pattern.matcher( _ ).matches() ){ "value" ->> _ :: HNil }
    }

    val required = Validation.empty[String]( "required" )( _.nonEmpty )
}

object string extends string