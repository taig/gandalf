package io.taig.gandalf.predef.test

import io.taig.gandalf.core.And
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.test.Suite
import io.taig.gandalf.predef

class PredefTest extends Suite {
    "capitalize" should "uppercase the first character" in {
        "foo".confirm( predef.capitalize ) shouldBe Some( "Foo" )
        "Foo".confirm( predef.capitalize ) shouldBe Some( "Foo" )
        "FOO".confirm( predef.capitalize ) shouldBe Some( "FOO" )
        "foo bar".confirm( predef.capitalize ) shouldBe Some( "Foo bar" )
        "".confirm( predef.capitalize ) shouldBe Some( "" )
    }

    "contains" should "check if a String for a substring" in {
        "foobar".confirm( predef.contains( "foo" ) ) shouldBe Some( "foobar" )
        "foo".confirm( predef.contains( "foo" ) ) shouldBe Some( "foo" )
        "bar".confirm( predef.contains( "foo" ) ) shouldBe None
    }

    it should "check if a SeqLike has a certain element" in {
        Seq( 1, 2, 3 ).confirm( predef.contains( 1 ) ) shouldBe Some( Seq( 1, 2, 3 ) )
        List( 1, 2, 3 ).confirm( predef.contains( 1 ) ) shouldBe Some( List( 1, 2, 3 ) )
        Seq( 1, 2, 3 ).confirm( predef.contains( 4 ) ) shouldBe None
        List( 1, 2, 3 ).confirm( predef.contains( 4 ) ) shouldBe None
    }

    it should "check if an Array has a certain element" in {
        val array = Array( 1, 2, 3 )
        array.confirm( predef.contains( 1 ) ) shouldBe Some( array )
        array.confirm( predef.contains( 4 ) ) shouldBe None
    }

    "email" should "check if input has valid format" in {
        "hello@taig.io".confirm( predef.email ) shouldBe Some( "hello@taig.io" )
        "".confirm( predef.email ) shouldBe None
        "hello".confirm( predef.email ) shouldBe None
        "hello@".confirm( predef.email ) shouldBe None
        "hello@taig".confirm( predef.email ) shouldBe None
        "@taig.io".confirm( predef.email ) shouldBe None
    }

    "empty" should "check if a String is empty" in {
        "".confirm( predef.empty ) shouldBe Some( "" )
        "foo".confirm( predef.empty ) shouldBe None
        " ".confirm( predef.empty ) shouldBe None
    }

    it should "check if a Traversable is empty" in {
        List().confirm( predef.empty ) shouldBe Some( List() )
        Seq( 1, 2, 3 ).confirm( predef.empty ) shouldBe None
    }

    it should "check if an Array is empty" in {
        val empty = Array()
        empty.confirm( predef.empty ) shouldBe Some( empty )
        Array( 1, 2, 3 ).confirm( predef.empty ) shouldBe None
    }

    "equal" should "check if two objects are equal" in {
        "foobar".confirm( predef.equal( "foobar" ) ) shouldBe Some( "foobar" )
        "foo".confirm( predef.equal( "foobar" ) ) shouldBe None
        42.confirm( predef.equal( 42 ) ) shouldBe Some( 42 )
        21.confirm( predef.equal( 42 ) ) shouldBe None
    }

    "length.max" should "check if a String has a maximum length" in {
        "foo".confirm( predef.length.max( 5 ) ) shouldBe Some( "foo" )
        "foo".confirm( predef.length.max( 3 ) ) shouldBe Some( "foo" )
        "foo".confirm( predef.length.max( 1 ) ) shouldBe None
    }

    it should "check if a Traversable has a maximum length" in {
        List( 1, 2, 3 ).confirm( predef.length.max( 5 ) ) shouldBe
            Some( List( 1, 2, 3 ) )
        List( 1, 2, 3 ).confirm( predef.length.max( 3 ) ) shouldBe
            Some( List( 1, 2, 3 ) )
        List( 1, 2, 3 ).confirm( predef.length.max( 1 ) ) shouldBe
            None
    }

    it should "check if an Array has a maximum length" in {
        val array = Array( 1, 2, 3 )
        array.confirm( predef.length.max( 5 ) ) shouldBe
            Some( array )
        array.confirm( predef.length.max( 3 ) ) shouldBe
            Some( array )
        array.confirm( predef.length.max( 1 ) ) shouldBe
            None
    }

    "length.min" should "check if a String has a minimum length" in {
        "foo".confirm( predef.length.min( 1 ) ) shouldBe Some( "foo" )
        "foo".confirm( predef.length.min( 3 ) ) shouldBe Some( "foo" )
        "foo".confirm( predef.length.min( 5 ) ) shouldBe None
    }

    it should "check if a Traversable has a minimum length" in {
        List( 1, 2, 3 ).confirm( predef.length.min( 1 ) ) shouldBe
            Some( List( 1, 2, 3 ) )
        List( 1, 2, 3 ).confirm( predef.length.min( 3 ) ) shouldBe
            Some( List( 1, 2, 3 ) )
        List( 1, 2, 3 ).confirm( predef.length.min( 5 ) ) shouldBe
            None
    }

    it should "check if an Array has a minimum length" in {
        val array = Array( 1, 2, 3 )
        array.confirm( predef.length.min( 1 ) ) shouldBe
            Some( array )
        array.confirm( predef.length.min( 3 ) ) shouldBe
            Some( array )
        array.confirm( predef.length.min( 5 ) ) shouldBe
            None
    }

    "lowercase" should "replace all uppercase letters" in {
        "foo".confirm( predef.lowercase ) shouldBe Some( "foo" )
        "Foo".confirm( predef.lowercase ) shouldBe Some( "foo" )
        "FooBar".confirm( predef.lowercase ) shouldBe Some( "foobar" )
        "".confirm( predef.lowercase ) shouldBe Some( "" )
    }

    "matches" should "check if a regex pattern matches a String" in {
        "foo".confirm( predef.matches( ".*foo.*" ) ) shouldBe Some( "foo" )
        "foobar".confirm( predef.matches( ".*foo.*" ) ) shouldBe Some( "foobar" )
        "bar".confirm( predef.matches( ".*foo.*" ) ) shouldBe None
    }

    "required" should "remove all whitespace and check if String is empty" in {
        "foo".confirm( predef.required )( And.validation ) shouldBe Some( "foo" )
        "foo".confirm( predef.required ) shouldBe Some( "foo" )
        "  foo".confirm( predef.required ) shouldBe Some( "foo" )
        "foo   ".confirm( predef.required ) shouldBe Some( "foo" )
        "  foo   ".confirm( predef.required ) shouldBe Some( "foo" )
        "     ".confirm( predef.required ) shouldBe None
    }

    "trim" should "remove all whitespace" in {
        "foo".confirm( predef.trim ) shouldBe Some( "foo" )
        "  foo".confirm( predef.trim ) shouldBe Some( "foo" )
        "foo   ".confirm( predef.trim ) shouldBe Some( "foo" )
        "  foo   ".confirm( predef.trim ) shouldBe Some( "foo" )
        "     ".confirm( predef.trim ) shouldBe Some( "" )
    }

    "trim.left" should "remove whitespace at the beginning" in {
        "foo".confirm( predef.trim.left ) shouldBe Some( "foo" )
        "  foo".confirm( predef.trim.left ) shouldBe Some( "foo" )
        "foo   ".confirm( predef.trim.left ) shouldBe Some( "foo   " )
        "  foo   ".confirm( predef.trim.left ) shouldBe Some( "foo   " )
        "     ".confirm( predef.trim.left ) shouldBe Some( "" )
    }

    "trim.right" should "remove whitespace at the end" in {
        "foo".confirm( predef.trim.right ) shouldBe Some( "foo" )
        "  foo".confirm( predef.trim.right ) shouldBe Some( "  foo" )
        "foo   ".confirm( predef.trim.right ) shouldBe Some( "foo" )
        "  foo   ".confirm( predef.trim.right ) shouldBe Some( "  foo" )
        "     ".confirm( predef.trim.right ) shouldBe Some( "" )
    }

    "uppercase" should "replace all lowercase letters" in {
        "foo".confirm( predef.uppercase ) shouldBe Some( "FOO" )
        "Foo".confirm( predef.uppercase ) shouldBe Some( "FOO" )
        "FooBar".confirm( predef.uppercase ) shouldBe Some( "FOOBAR" )
        "".confirm( predef.uppercase ) shouldBe Some( "" )
    }

    "url" should "check if input has valid format" in {
        "http://taig.io/".confirm( predef.url ) shouldBe Some( "http://taig.io/" )
        "taig.io".confirm( predef.url ) shouldBe Some( "taig.io" )
        "http://taig.io#foobar".confirm( predef.url ) shouldBe Some( "http://taig.io#foobar" )
        "http://taig".confirm( predef.url ) shouldBe None
        "taig".confirm( predef.url ) shouldBe None
        "".confirm( predef.url ) shouldBe None
    }
}