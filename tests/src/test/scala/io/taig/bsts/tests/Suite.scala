package io.taig.bsts.tests

import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.report.Report
import io.taig.bsts.predef._
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }
import shapeless._
import shapeless.record._
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

    object report {
        implicit val required = Report( rule.required )( _ ⇒ "Pflichtfeld" )

        implicit val min = Report( rule.min _ )( args ⇒ s"Mindestens ${args( "expected" )} Zeichen" )

        implicit val max = Report( rule.max _ )( args ⇒ s"Maximal ${args( "expected" )} Zeichen" )

        implicit val parse = Report( mutation.parse )( _ ⇒ "Keine gültige Zahl" )
    }

    override def beforeAll() = {
        rule.required.validate( "foo" ).isValid shouldBe true
        rule.required.validate( "" ).isValid shouldBe false

        intercept[IllegalStateException] {
            rule.blow.validate( "" )
        }

        rule.min( 3 ).validate( "foo" ).isValid shouldBe true
        rule.min( 3 ).validate( "foobar" ).isValid shouldBe true
        rule.min( 3 ).validate( "" ).isValid shouldBe false

        rule.max( 3 ).validate( "" ).isValid shouldBe true
        rule.max( 3 ).validate( "foo" ).isValid shouldBe true
        rule.max( 3 ).validate( "foobar" ).isValid shouldBe false

        transformation.trim.validate( "" ) shouldBe ""
        transformation.trim.validate( "asdf" ) shouldBe "asdf"
        transformation.trim.validate( "  asdf   " ) shouldBe "asdf"

        mutation.parse.validate( "5" ) shouldBe Valid( 5 )
        mutation.parse.validate( "" ).leftMap( _.toString ) shouldBe Invalid( "Error(parse)" )
    }
}