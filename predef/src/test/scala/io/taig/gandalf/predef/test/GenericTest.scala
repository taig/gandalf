package io.taig.gandalf.predef.test

import cats.data.Validated._
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.test.Suite
import io.taig.gandalf.predef.generic.{ equal ⇒ equalle }

class GenericTest extends Suite {
    "equal" should "check if two objects are equal" in {
        equalle( "foobar" ).validate( "foobar" ) shouldBe valid( "foobar" )
        equalle( "foobar" ).validate( "foo" ) shouldBe invalidNel( "equal" )
        equalle( 42 ).validate( 42 ) shouldBe valid( 42 )
        equalle( 42 ).validate( 7 ) shouldBe invalidNel( "equal" )
    }
}