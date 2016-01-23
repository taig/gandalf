package io.taig.bsts

import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

class PolicyTest extends Suite {
    "apply" should "allow to create a Policy from a Rule" in {
        val rule = Rule.empty[String]( "required" )( _.nonEmpty )

        Policy( rule ) shouldBe a[Policy[_, _]]
    }

    "validate" should "yield a Validation" in {
        val policy = Policy( Rule.empty[String]( "required" )( _.nonEmpty ) )

        policy.validate( "" ).isSuccess shouldBe false
        policy.validate( "" ).isFailure shouldBe true
        policy.validate( "foo" ).isSuccess shouldBe true
        policy.validate( "foo" ).isFailure shouldBe false
    }

    it should "provide a report representation" in {
        def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        implicit val r1 = Report( min _ ) as { arguments ⇒
            s"Value too short, expected: ${arguments( "expected" )}, actual: ${arguments( "actual" )}"
        }

        Policy( min( 4 ) ).validate( "foobar" ).report shouldBe Nil
        Policy( min( 4 ) ).validate( "foo" ).report shouldBe List( "Value too short, expected: 4, actual: 3" )

        def max( length: Int ) = Rule[String, Int]( "max" )( _.length )( _ <= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        implicit val r2 = Report( max _ ) as { arguments ⇒
            s"Value too long, expected: ${arguments( "expected" )}, actual: ${arguments( "actual" )}"
        }

        Policy( min( 2 ) :: max( 10 ) :: HNil ).validate( "foobar" ).report shouldBe Nil
        Policy( min( 2 ) :: max( 10 ) :: HNil ).validate( "foobarfoobar" ).report shouldBe List(
            "Value too long, expected: 10, actual: 12"
        )
        Policy( min( 10 ) :: max( 2 ) :: HNil ).validate( "foobar" ).report shouldBe List(
            "Value too short, expected: 10, actual: 6",
            "Value too long, expected: 2, actual: 6"
        )
    }

    it should "provide a raw representation" in {
        def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        implicit val r1 = Report( min _ ) as { arguments ⇒
            s"Value too short, expected: ${arguments( "expected" )}, actual: ${arguments( "actual" )}"
        }

        Policy( min( 4 ) ).validate( "foobar" ).raw shouldBe Nil
        Policy( min( 4 ) ).validate( "foo" ).raw shouldBe List( ( "min", List( "foo", 4, 3 ) ) )

        def max( length: Int ) = Rule[String, Int]( "max" )( _.length )( _ <= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        implicit val r2 = Report( max _ ) as { arguments ⇒
            s"Value too long, expected: ${arguments( "expected" )}, actual: ${arguments( "actual" )}"
        }

        Policy( min( 2 ) :: max( 10 ) :: HNil ).validate( "foobar" ).raw shouldBe Nil
        Policy( min( 2 ) :: max( 10 ) :: HNil ).validate( "foobarfoobar" ).raw shouldBe List(
            ( "max", List( "foobarfoobar", 10, 12 ) )
        )
        Policy( min( 10 ) :: max( 2 ) :: HNil ).validate( "foobar" ).raw shouldBe List(
            ( "min", List( "foobar", 10, 6 ) ),
            ( "max", List( "foobar", 2, 6 ) )
        )
    }

    it should "have a useful string representation" in {
        val required = Rule.empty[String]( "required" )( _.nonEmpty )

        Policy( required ).validate( "foo" ).toString shouldBe "Success"
        Policy( required ).validate( "" ).toString shouldBe "Failures(required)"

        val min = ( length: Int ) ⇒ Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        Policy( min( 4 ) ).validate( "foobar" ).toString shouldBe "Success"
        Policy( min( 4 ) ).validate( "foo" ).toString shouldBe "Failures((min, (foo, 4, 3)))"

        ( required combine min( 4 ) ).validate( "foobar" ).toString shouldBe "Success"
        ( required combine min( 4 ) ).validate( "" ).toString shouldBe "Failures(required, (min, (, 4, 0)))"
    }

    "combine" should "with a rule should create a new policy" in {
        val r1 = Rule.empty[String]( "required" )( _.nonEmpty )
        val r2 = Rule.empty[String]( "notRequired" )( _.isEmpty )

        ( r1 combine r2 ) shouldBe Policy( r1 :: r2 :: HNil )
    }
}