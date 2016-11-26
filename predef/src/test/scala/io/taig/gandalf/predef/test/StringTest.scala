//package io.taig.gandalf.predef.test
//
//import cats.data.Validated._
//import io.taig.gandalf.core.goaway.syntax.all._
//import io.taig.gandalf.core.test.Suite
//import io.taig.gandalf.predef.string._
//import io.taig.gandalf.predef.string.{ empty ⇒ isEmpty }
//
//class StringTest extends Suite {
//    "email" should "check if input has valid format" in {
//        "hello@taig.io".confirm( email ) shouldBe Some( "hello@taig.io" )
//        email.validate( "" ) shouldBe invalidNel( "email" )
//        "hello".confirm( email ) shouldBe invalidNel( "email" )
//        "hello@".confirm( email ) shouldBe invalidNel( "email" )
//        "hello@taig".confirm( email ) shouldBe invalidNel( "email" )
//        "@taig.io".confirm( email ) shouldBe invalidNel( "email" )
//    }
//
//    "empty" should "check if input is empty" in {
//        isEmpty.validate( "" ) shouldBe Some( "" )
//        "foo".confirm( isEmpty ) shouldBe invalidNel( "empty" )
//        " ".confirm( isEmpty ) shouldBe invalidNel( "empty" )
//    }
//
//    "ltrim" should "remove whitespace at the beginning" in {
//        "foo".confirm( ltrim ) shouldBe Some( "foo" )
//        "  foo".confirm( ltrim ) shouldBe Some( "foo" )
//        "foo   ".confirm( ltrim ) shouldBe Some( "foo   " )
//        "  foo   ".confirm( ltrim ) shouldBe Some( "foo   " )
//        "     ".confirm( ltrim ) shouldBe Some( "" )
//    }
//
//    "matches" should "check if a given regex pattern matches" in {
//        matches( ".*foo.*" ).validate( "foo" ) shouldBe Some( "foo" )
//        matches( ".*foo.*" ).validate( "foobar" ) shouldBe Some( "foobar" )
//        matches( ".*foo.*" ).validate( "bar" ) shouldBe invalidNel( "matches" )
//    }
//
//    "required" should "remove all whitespace and check if input is empty" in {
//        "foo".confirm( required ) shouldBe Some( "foo" )
//        "  foo".confirm( required ) shouldBe Some( "foo" )
//        "foo   ".confirm( required ) shouldBe Some( "foo" )
//        "  foo   ".confirm( required ) shouldBe Some( "foo" )
//        "     ".confirm( required ) shouldBe invalidNel( "not(empty)" )
//    }
//
//    "rtrim" should "remove whitespace at the end" in {
//        "foo".confirm( rtrim ) shouldBe Some( "foo" )
//        "  foo".confirm( rtrim ) shouldBe Some( "  foo" )
//        "foo   ".confirm( rtrim ) shouldBe Some( "foo" )
//        "  foo   ".confirm( rtrim ) shouldBe Some( "  foo" )
//        "     ".confirm( rtrim ) shouldBe Some( "" )
//    }
//
//    "toLower" should "replace all uppercase letters" in {
//        "foo".confirm( toLower ) shouldBe Some( "foo" )
//        "Foo".confirm( toLower ) shouldBe Some( "foo" )
//        "FooBar".confirm( toLower ) shouldBe Some( "foobar" )
//    }
//
//    "toUpper" should "replace all lowercase letters" in {
//        "FOO".confirm( toUpper ) shouldBe Some( "FOO" )
//        "Foo".confirm( toUpper ) shouldBe Some( "FOO" )
//        "FooBar".confirm( toUpper ) shouldBe Some( "FOOBAR" )
//    }
//
//    "url" should "check if input has valid format" in {
//        "http://taig.io/".confirm( url ) shouldBe Some( "http://taig.io/" )
//        "taig.io".confirm( url ) shouldBe Some( "taig.io" )
//        "http://taig.io#foobar".confirm( url ) shouldBe Some( "http://taig.io#foobar" )
//        "http://taig".confirm( url ) shouldBe invalidNel( "url" )
//        "taig".confirm( url ) shouldBe invalidNel( "url" )
//    }
//}