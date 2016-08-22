package io.taig.gandalf.tests

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.predef.all._
import io.taig.gandalf.syntax.all._

class SyntaxTest extends Suite {
    "~>" should "allow to mutate data" in {
        ( trim ~> toUpper ).validate( "  Foo  " ) shouldBe valid( "FOO" )
        //        ( trim ~> required ).validate( "foo" ) shouldBe valid( "foo" )
        //        ( trim ~> required ).validate( " foo  " ) shouldBe valid( "foo" )
        //        ( trim ~> required ).validate( "" ) shouldBe invalidNel( "Required" )
        //        ( trim ~> required ).validate( "   " ) shouldBe invalidNel( "Required" )
    }

    //    it should "be aliased by ~>" in {
    //        ( trim <~> required ).getClass shouldBe ( trim ~> required ).getClass
    //    }
    //
    //    "<*>" should "allow to mutate data" in {
    //        ( isDefined[String] <*> required ).validate( Some( "foo" ) ) shouldBe valid( "foo" )
    //        ( isDefined[String] <*> required ).validate( Some( "" ) ) shouldBe invalidNel( "Required" )
    //        ( isDefined[String] <*> required ).validate( None ) shouldBe invalidNel( "IsDefined" )
    //    }
    //
    //    it should "be aliased by ~>" in {
    //        ( isDefined[String] <*> required ).getClass shouldBe ( isDefined[String] ~> required ).getClass
    //    }
    //
    //    "&&" should "mimic the logical AND" in {
    //        ( required && matches( "foo" ) ).validate( "foo" ) shouldBe valid( "foo" )
    //        ( required && matches( "foo" ) ).validate( "foobar" ) shouldBe invalidNel( "Matches" )
    //        ( required && matches( "foo" ) ).validate( "" ) shouldBe invalidNel( "Required" )
    //    }
    //
    //    "&" should "mimic the bitwise AND" in {
    //        ( required & matches( "foo" ) ).validate( "foo" ) shouldBe valid( "foo" )
    //        ( required & matches( "foo" ) ).validate( "foobar" ) shouldBe invalidNel( "Matches" )
    //        ( required & matches( "foo" ) ).validate( "" ) shouldBe invalid( NonEmptyList( "Required", "Matches" ) )
    //    }
    //
    //    "||" should "mimic the logical OR" in {
    //        ( required || matches( "foo" ) ).validate( "foo" ) shouldBe valid( "foo" )
    //        ( required || matches( "foo" ) ).validate( "foobar" ) shouldBe valid( "foobar" )
    //        ( required || matches( "foo" ) ).validate( "" ) shouldBe invalid( NonEmptyList( "Required", "Matches" ) )
    //    }
}