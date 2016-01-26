package io.taig.bsts

import shapeless._
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
}