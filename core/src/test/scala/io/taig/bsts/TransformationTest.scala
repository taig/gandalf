package io.taig.bsts

import shapeless._
import shapeless.syntax.singleton._

import scala.util.Try

class TransformationTest extends Suite {
    "apply" should "allow to create simple transformations" in {
        val builder = Transformation[String, Int]( "parse" )

        builder shouldBe a[Transformation.Builder1[_, _, _]]

        val transformation = builder( string ⇒ Try( string.toInt ).toOption )

        transformation shouldBe a[Transformation[_, _, _, _]]
        transformation.validate( "5" ).isSuccess shouldBe true
        transformation.validate( "foo" ).isSuccess shouldBe false
    }

    it should "allow to create transformations with validation arguments" in {
        val builder = Transformation[String, Int]( "parse" )

        builder shouldBe a[Transformation.Builder1[_, _, _]]

        val transformation = builder( string ⇒ Try( string.toInt ).toOption ) { value ⇒
            "value" ->> value :: HNil
        }

        transformation shouldBe a[Transformation[_, _, _, _]]
        transformation.validate( "5" ).isSuccess shouldBe true
        transformation.validate( "foo" ).isSuccess shouldBe false
    }

    it should "allow to create a transformation from a function" in {
        val transformation = Transformation[String, Int]( "parse" ) from ( _.toInt )

        transformation shouldBe a[Transformation[_, _, _, _]]
        transformation.validate( "5" ).isSuccess shouldBe true

        intercept[NumberFormatException] {
            transformation.validate( "foo" ).isSuccess shouldBe false
        }
    }
}