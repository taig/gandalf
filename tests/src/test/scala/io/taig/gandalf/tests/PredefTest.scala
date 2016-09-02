//package io.taig.gandalf.tests
//
//import cats.data.Validated._
//import io.taig.gandalf.syntax.all._
//import io.taig.gandalf.predef.string._
//
//class PredefTest extends Suite {
//    //    "asIterable" should "transform Strings to Iterables" in {
//    //        asIterable.validate( "foo" ) shouldBe valid( List( 'f', 'o', 'o' ) )
//    //    }
//
//    //    "IsDefined" should "validate Options" in {
//    //        isDefined[String].validate( Some( "foo" ) ) shouldBe valid( "foo" )
//    //        isDefined[String].validate( None ) shouldBe invalidNel( "IsDefined" )
//    //    }
//
//    //    "eq" should "check for equality" in {
//    //        eq["foo"].validate( "foo" ) shouldBe valid( "foo" )
//    //        matches["foo"].validate( "foobar" ) shouldBe invalidNel( "matches" )
//    //    }
//    //
//    //    "isEmpty" should "check whether an Iterable is empty" in {
//    //        isEmpty[Int].validate( List() ) shouldBe valid( List() )
//    //        isEmpty[Int].validate( List( 1 ) ) shouldBe invalidNel( "isEmpty" )
//    //        isEmpty[Int].validate( List( 1, 2, 3 ) ) shouldBe invalidNel( "isEmpty" )
//    //    }
//    //
//    //    "nonEmpty" should "check whether an Iterable is empty" in {
//    //        nonEmpty[Int].validate( List( 1, 2, 3 ) ) shouldBe valid( List( 1, 2, 3 ) )
//    //        nonEmpty[Int].validate( List( 1 ) ) shouldBe valid( List( 1 ) )
//    //        nonEmpty[Int].validate( List() ) shouldBe invalidNel( "nonEmpty" )
//    //    }
//
//    //    "Regex" should "check if a given pattern matches" in {
//    //        Regex.regex( ".*foo.*" ).validate( "foo" ) shouldBe valid( "foo" )
//    //        Regex.regex( ".*foo.*" ).validate( "foobar" ) shouldBe valid( "foobar" )
//    //        Regex.regex( ".*foo.*" ).validate( "bar" ) shouldBe invalidNel( "Regex" )
//    //    }
//
//    "required" should "check whether a String is empty" in {
//        required.validate( "foo" ) shouldBe valid( "foo" )
//        required.validate( "" ) shouldBe invalidNel( "required" )
//        required.validate( " " ) shouldBe invalidNel( "required" )
//    }
//
//    "toLower" should "replace all uppercase letters in a String" in {
//        toLower.validate( "foo" ) shouldBe valid( "foo" )
//        toLower.validate( "Foo" ) shouldBe valid( "foo" )
//        toLower.validate( "FooBar" ) shouldBe valid( "foobar" )
//    }
//
//    "toUpper" should "replace all lowercase letters in a String" in {
//        toUpper.validate( "FOO" ) shouldBe valid( "FOO" )
//        toUpper.validate( "Foo" ) shouldBe valid( "FOO" )
//        toUpper.validate( "FooBar" ) shouldBe valid( "FOOBAR" )
//    }
//
//    "trim" should "remove all trailing whitespace from a String" in {
//        trim.validate( "foo" ) shouldBe valid( "foo" )
//        trim.validate( "  foo" ) shouldBe valid( "foo" )
//        trim.validate( "foo   " ) shouldBe valid( "foo" )
//        trim.validate( "  foo   " ) shouldBe valid( "foo" )
//        trim.validate( "     " ) shouldBe valid( "" )
//    }
//}