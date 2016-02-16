package io.taig.bsts.tests

import cats.data.Xor._
import io.taig.bsts.mutation.Mutation
import io.taig.bsts.rule.Rule
import io.taig.bsts.transformation.Transformation
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }
import shapeless._
import shapeless.syntax.singleton._

import scala.util.Try

trait Suite
        extends FlatSpec
        with Matchers
        with BeforeAndAfterAll {
    object rule {
        val required = Rule[String]( "required" )( _.nonEmpty )

        val blow = Rule[String]( "blow" )( _ ⇒ throw new IllegalStateException )

        def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        def max( length: Int ) = Rule[String, Int]( "max" )( _.length )( _ <= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        def gt( size: Int ) = Rule[Int]( "gt" )( _ > size )
    }

    object transformation {
        val trim = Transformation[String, String]( "trim" )( _.trim )

        def identity[T] = Transformation[T, T]( "identity" )( Predef.identity )
    }

    object mutation {
        val parse = Mutation[String, Int]( "parse" )( s ⇒ Try( s.toInt ) )
    }

    override def beforeAll() = {
        rule.required.validate( "foo" ).isRight shouldBe true
        rule.required.validate( "" ).isRight shouldBe false

        intercept[IllegalStateException] {
            rule.blow.validate( "" )
        }

        rule.min( 3 ).validate( "foo" ).isRight shouldBe true
        rule.min( 3 ).validate( "foobar" ).isRight shouldBe true
        rule.min( 3 ).validate( "" ).isRight shouldBe false

        rule.max( 3 ).validate( "" ).isRight shouldBe true
        rule.max( 3 ).validate( "foo" ).isRight shouldBe true
        rule.max( 3 ).validate( "foobar" ).isRight shouldBe false

        transformation.trim.validate( "" ) shouldBe ""
        transformation.trim.validate( "asdf" ) shouldBe "asdf"
        transformation.trim.validate( "  asdf   " ) shouldBe "asdf"

        mutation.parse.validate( "5" ) shouldBe Right( 5 )
        mutation.parse.validate( "" ).leftMap( _.toString ) shouldBe Left( "Error(parse)" )
    }
}