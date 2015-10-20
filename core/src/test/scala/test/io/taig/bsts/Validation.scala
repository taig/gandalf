package test.io.taig.bsts

import io.taig.bsts.rule._
import io.taig.bsts.{ Validation ⇒ validation, _ }
import io.taig.bsts.default.show._
import shapeless.HNil

class Validation extends Suite {
    implicitly[Show[Email]]

    it should "work with single elements" in {
        validation( Email ).validate( "asdf@asdf.asdf" ) shouldBe a[Success]
        validation( Email ).validate( "asdf@asdf" ) shouldBe a[Failure]
        validation( Phone ).validate( "12345" ) shouldBe a[Success]
    }

    it should "work with multiple elements" in {
        validation( Required[String]() :: Email :: Phone :: HNil ).validate( "asdf" ) shouldBe a[Failure]
        validation( Min[String]( 3 ) :: Max[String]( 6 ) :: HNil ).validate( "asdf" ) shouldBe a[Success]
    }

    it should "work with Required" in {
        validation( Required[Option[String]]() ).validate( None ) shouldBe a[Failure]
        validation( Required[Option[String]]() :: HNil ).validate( Some( "" ) ) shouldBe a[Success]
        validation( Required[String]() ).validate( "asdf" ) shouldBe a[Success]
        validation( Required[String]() :: HNil ).validate( "" ) shouldBe a[Failure]
    }

    it should "allow empty values" in {
        validation( Email :: Phone :: HNil ).validate( "" ) shouldBe a[Success]
    }

    it should "not allow empty values for the Required Rule" in {
        validation( Required[String]() :: HNil ).validate( "" ) shouldBe a[Failure]
        validation( Required[String]() :: Email :: Phone :: HNil ).validate( "" ) shouldBe a[Failure]
    }
}