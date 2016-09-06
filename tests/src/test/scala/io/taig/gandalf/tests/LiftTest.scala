//package io.taig.gandalf.tests
//
//import cats.data.Validated._
//import io.taig.gandalf._
//import io.taig.gandalf.predef.IsDefined._
//import io.taig.gandalf.predef._
//import io.taig.gandalf.predef.Required.nonEmpty
//import io.taig.gandalf.predef.Trim.trim
//import io.taig.gandalf.predef.messages._
//import io.taig.gandalf.syntax.all._
//
//class LiftTest extends Suite {
//    "tryLift" should "perform runtime validations" in {
//        tryLift[Required]( "foobar" ) shouldBe valid( "foobar" )
//        tryLift[Required]( "" ) shouldBe invalidNel( "Required" )
//
//        tryLift[Trim <~> Required]( "foobar" ) shouldBe valid( "foobar" )
//        tryLift[Trim <~> Required]( "  foobar   " ) shouldBe valid( "foobar" )
//        tryLift[Trim <~> Required]( "    " ) shouldBe invalidNel( "Required" )
//        tryLift[Trim <~> Required]( "" ) shouldBe invalidNel( "Required" )
//    }
//
//    it should "support the DSL" in {
//        tryLift( nonEmpty )( "foobar" ) shouldBe valid( "foobar" )
//        tryLift( nonEmpty )( "" ) shouldBe invalidNel( "Required" )
//
//        tryLift( trim ~> nonEmpty )( "foobar" ) shouldBe valid( "foobar" )
//        tryLift( trim ~> nonEmpty )( "  foobar   " ) shouldBe valid( "foobar" )
//        tryLift( trim ~> nonEmpty )( "    " ) shouldBe invalidNel( "Required" )
//        tryLift( trim ~> nonEmpty )( "" ) shouldBe invalidNel( "Required" )
//    }
//
//    it should "infer the input type" in {
//        //        tryLift[Option[String]]( Some( "foobar" ) ).into( isDefined ~> trim ~> nonEmpty ) shouldBe valid( "foobar" )
//    }
//
//    "lift" should "perform compile time validations" in {
//        lift[Required]( "foobar" ).value shouldBe "foobar"
//        assertTypeError( """lift[Required]( "" )""" )
//
//        lift[Trim <~> Required]( "foobar" ).value shouldBe "foobar"
//        lift[Trim <~> Required]( "   foobar    " ).value shouldBe "foobar"
//        assertTypeError( """lift[Trim <~> Required]( "   " )""" )
//        assertTypeError( """lift[Trim <~> Required]( "" )""" )
//    }
//
//    it should "support the DSL" in {
//        lift( nonEmpty )( "foobar" ).value shouldBe "foobar"
//        assertTypeError( """lift( nonEmpty )( "" )""" )
//
//        lift( trim ~> nonEmpty )( "foobar" ).value shouldBe "foobar"
//        lift( trim ~> nonEmpty )( "   foobar    " ).value shouldBe "foobar"
//        assertTypeError( """lift( trim ~> nonEmpty )( "   " )""" )
//        assertTypeError( """lift( trim ~> nonEmpty )( "" )""" )
//    }
//
//    it should "infer the input type" in {
//        //        lift( isDefined ~> trim ~> nonEmpty )( Some( "foobar" ) ) shouldBe valid( "foobar" )
//    }
//}