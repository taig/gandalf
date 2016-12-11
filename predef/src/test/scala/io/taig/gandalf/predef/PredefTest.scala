package io.taig.gandalf.predef

import io.taig.gandalf._
import io.taig.gandalf.syntax.all._
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

    "gt" should "compare numbers with the > operator" in {
        10.confirm( predef.gt( 5 ) ) shouldBe Some( 10 )
        5.confirm( predef.gt( 5 ) ) shouldBe None
        0.confirm( predef.gt( 5 ) ) shouldBe None
    }

    it should "compare numbers of different types" in {
        val byte = 5.toByte
        val short = 5.toShort
        10.confirm( predef.gt( byte ) ) shouldBe Some( 10 )
        10.confirm( predef.gt( 5d ) ) shouldBe Some( 10 )
        10.confirm( predef.gt( 5f ) ) shouldBe Some( 10 )
        10.confirm( predef.gt( 5l ) ) shouldBe Some( 10 )
        10.confirm( predef.gt( short ) ) shouldBe Some( 10 )
    }

    "gte" should "compare numbers with the >= operator" in {
        10.confirm( predef.gte( 5 ) ) shouldBe Some( 10 )
        5.confirm( predef.gte( 5 ) ) shouldBe Some( 5 )
        0.confirm( predef.gte( 5 ) ) shouldBe None
    }

    it should "compare numbers of different types" in {
        val byte = 5.toByte
        val short = 5.toShort
        10.confirm( predef.gte( byte ) ) shouldBe Some( 10 )
        10.confirm( predef.gte( 5d ) ) shouldBe Some( 10 )
        10.confirm( predef.gte( 5f ) ) shouldBe Some( 10 )
        10.confirm( predef.gte( 5l ) ) shouldBe Some( 10 )
        10.confirm( predef.gte( short ) ) shouldBe Some( 10 )
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

    "lt" should "compare numbers with the < operator" in {
        0.confirm( predef.lt( 5 ) ) shouldBe Some( 0 )
        5.confirm( predef.lt( 5 ) ) shouldBe None
        10.confirm( predef.lt( 5 ) ) shouldBe None
    }

    it should "compare numbers of different types" in {
        val byte = 5.toByte
        val short = 5.toShort
        0.confirm( predef.lt( byte ) ) shouldBe Some( 0 )
        0.confirm( predef.lt( 5d ) ) shouldBe Some( 0 )
        0.confirm( predef.lt( 5f ) ) shouldBe Some( 0 )
        0.confirm( predef.lt( 5l ) ) shouldBe Some( 0 )
        0.confirm( predef.lt( short ) ) shouldBe Some( 0 )
    }

    "lte" should "compare numbers with the <= operator" in {
        0.confirm( predef.lte( 5 ) ) shouldBe Some( 0 )
        5.confirm( predef.lte( 5 ) ) shouldBe Some( 5 )
        10.confirm( predef.lte( 5 ) ) shouldBe None
    }

    it should "compare numbers of different types" in {
        val byte = 5.toByte
        val short = 5.toShort
        0.confirm( predef.lte( byte ) ) shouldBe Some( 0 )
        0.confirm( predef.lte( 5d ) ) shouldBe Some( 0 )
        0.confirm( predef.lte( 5f ) ) shouldBe Some( 0 )
        0.confirm( predef.lte( 5l ) ) shouldBe Some( 0 )
        0.confirm( predef.lte( short ) ) shouldBe Some( 0 )
    }

    "matches" should "check if a regex pattern matches a String" in {
        "foo".confirm( predef.matches( ".*foo.*" ) ) shouldBe Some( "foo" )
        "foobar".confirm( predef.matches( ".*foo.*" ) ) shouldBe Some( "foobar" )
        "bar".confirm( predef.matches( ".*foo.*" ) ) shouldBe None
    }

    "negative" should "check if a number is < 0" in {
        -5.confirm( predef.negative ) shouldBe Some( -5 )
        0.confirm( predef.negative ) shouldBe None
        5.confirm( predef.negative ) shouldBe None
    }

    "positive" should "check if a number is > 0" in {
        5.confirm( predef.positive ) shouldBe Some( 5 )
        0.confirm( predef.positive ) shouldBe None
        -5.confirm( predef.positive ) shouldBe None
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

    "zero" should "check if a number is 0" in {
        0.confirm( predef.zero ) shouldBe Some( 0 )
        0f.confirm( predef.zero ) shouldBe Some( 0 )
        0l.confirm( predef.zero ) shouldBe Some( 0 )
        0d.confirm( predef.zero ) shouldBe Some( 0 )
    }
}