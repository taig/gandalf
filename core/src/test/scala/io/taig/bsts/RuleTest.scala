package io.taig.bsts

import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

class RuleTest extends Suite {
    "apply" should "allow to create simple (no-arg) rules" in {
        val builder = Rule.empty[String]( "required" )

        builder shouldBe a[Rule.Builder0[_, _]]

        val rule = builder( _.nonEmpty )

        rule shouldBe a[Rule[_, _, _]]
    }

    it should "allow to create complex rules" in {
        val builder = Rule[String]( "match" )

        builder shouldBe a[Rule.Builder1[_, _]]

        val rule = ( compare: String ) ⇒ builder( _ == compare ) { value ⇒
            "expected" ->> value :: HNil
        }

        rule( "reference" ) shouldBe a[Rule[_, _, _]]
    }

    it should "allow to create complex rules with transformations" in {
        val builder = Rule[String, Int]( "min" )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _.length )( _ >= length ) { ( value, actual ) ⇒
            "expected" ->> length :: HNil
        }

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
    }

    "validate" should "yield a Validation" in {
        val rule = Rule.empty[String]( "required" )( _.nonEmpty )

        rule.validate( "" ) shouldBe a[Validation[_, _, _]]
        rule.validate( "foo" ) shouldBe a[Validation[_, _, _]]
    }

    it should "provide a report representation" in {
        val min = ( length: Int ) ⇒ Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        implicit val report = Report( min ) as { arguments ⇒
            s"Value too short, expected: ${arguments( "expected" )}, actual: ${arguments( "actual" )}"
        }

        min( 4 ).validate( "foobar" ).report shouldBe None
        min( 4 ).validate( "foo" ).report shouldBe Some( "Value too short, expected: 4, actual: 3" )
    }

    it should "provide a raw representation" in {
        val min = ( length: Int ) ⇒ Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        min( 4 ).validate( "foobar" ).raw shouldBe None
        min( 4 ).validate( "foo" ).raw shouldBe Some( "min", List( "foo", 4, 3 ) )
    }

    it should "have a useful string representation" in {
        val required = Rule.empty[String]( "required" )( _.nonEmpty )

        required.validate( "foo" ).toString shouldBe "Success"
        required.validate( "" ).toString shouldBe "Failure(required)"

        val min = ( length: Int ) ⇒ Rule[String, Int]( "min" )( _.length )( _ >= length ) { ( value, actual ) ⇒
            "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
        }

        min( 4 ).validate( "foobar" ).toString shouldBe "Success"
        min( 4 ).validate( "foo" ).toString shouldBe "Failure(min, (foo, 4, 3))"
    }

    "combine" should "create a policy from 2 rules" in {
        val r1 = Rule.empty[String]( "required" )( _.nonEmpty )
        val r2 = Rule.empty[String]( "notRequired" )( _.isEmpty )

        r1 combine r2 shouldBe a[Policy[_, _]]
    }
}