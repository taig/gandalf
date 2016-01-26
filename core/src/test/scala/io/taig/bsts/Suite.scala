package io.taig.bsts

import org.scalatest.{ BeforeAndAfterAll, Matchers, FlatSpec }
import shapeless._
import shapeless.syntax.singleton._

trait Suite
        extends FlatSpec
        with Matchers
        with BeforeAndAfterAll {
    object rule {
        val required = Rule.empty[String]( "required" )( _.nonEmpty )

        val blow = Rule.empty[String]( "blow" )( _ ⇒ throw new IllegalStateException() )

        def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        def max( length: Int ) = Rule[String, Int]( "max" )( _.length )( _ <= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }
    }

    override def beforeAll() = {
        rule.required.validate( "foo" ).isSuccess shouldBe true
        rule.required.validate( "" ).isSuccess shouldBe false

        intercept[IllegalStateException] {
            rule.blow.validate( "" )
        }

        rule.min( 3 ).validate( "foo" ).isSuccess shouldBe true
        rule.min( 3 ).validate( "foobar" ).isSuccess shouldBe true
        rule.min( 3 ).validate( "" ).isSuccess shouldBe false

        rule.max( 3 ).validate( "" ).isSuccess shouldBe true
        rule.max( 3 ).validate( "foo" ).isSuccess shouldBe true
        rule.max( 3 ).validate( "foobar" ).isSuccess shouldBe false
    }
}