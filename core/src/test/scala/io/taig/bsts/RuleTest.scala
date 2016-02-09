package io.taig.bsts

import shapeless._
import shapeless.syntax.singleton._

class RuleTest extends Suite {
    "apply" should "allow to create simple rules" in {
        val builder = Rule[String]( "required" )

        builder shouldBe a[Rule.Builder1[_, _]]

        val rule = builder( _.nonEmpty )

        rule shouldBe a[Rule[_, _, _]]
        rule.validate( "foo" ).isSuccess shouldBe true
        rule.validate( "" ).isSuccess shouldBe false
    }

    it should "allow to create rules with validation arguments" in {
        val builder = Rule[String]( "match" )

        builder shouldBe a[Rule.Builder1[_, _]]

        val rule = ( compare: String ) ⇒ builder( _ == compare ) { value ⇒
            "expected" ->> value :: HNil
        }

        rule( "foo" ) shouldBe a[Rule[_, _, _]]
        rule( "foo" ).validate( "foo" ).isSuccess shouldBe true
        rule( "foo" ).validate( "" ).isSuccess shouldBe false
    }

    it should "allow to create simple rules with transformations" in {
        val builder = Rule[String, Int]( "min" )( _.length )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _ >= length )

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
        rule( 3 ).validate( "foobar" ).isSuccess shouldBe true
        rule( 3 ).validate( "" ).isSuccess shouldBe false
    }

    it should "allow to create simple rules with transformations and validation arguments" in {
        val builder = Rule[String, Int]( "min" )( _.length )

        builder shouldBe a[Rule.Builder2[_, _, _]]

        val rule = ( length: Int ) ⇒ builder( _ >= length ) { ( value, actual ) ⇒
            "expected" ->> length :: HNil
        }

        rule( Int.MaxValue ) shouldBe a[Rule[_, _, _]]
        rule( 3 ).validate( "foobar" ).isSuccess shouldBe true
        rule( 3 ).validate( "" ).isSuccess shouldBe false
    }
}