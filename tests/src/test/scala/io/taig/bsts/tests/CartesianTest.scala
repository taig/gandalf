package io.taig.bsts.tests

import cats.data.NonEmptyList
import cats.data.Validated.Invalid
import cats.std.list._
import cats.syntax.cartesian._
import io.taig.bsts.syntax.cartesian._
import io.taig.bsts.syntax.dsl._
import io.taig.bsts.syntax.raw._
import io.taig.bsts.report.syntax.report._
import io.taig.bsts.report.syntax.cartesian._

class CartesianTest extends Suite {
    it should "be possible to combine Term validation raws" in {
        ( rule.required.validate( "foo" ).raw |@| rule.required.validate( "" ).raw ) map { ( _, _ ) } shouldBe
            Invalid( NonEmptyList( ( "required", Nil ) ) )
    }

    it should "be possible to combine Policy validation raws" in {
        ( ( rule.required && rule.min( 4 ) ).validate( "foo" ).raw |@|
            ( rule.required && rule.max( 8 ) ).validate( "foobar" ).raw ) map { ( _, _ ) } shouldBe
            Invalid( NonEmptyList( ( "min", "foo" :: 4 :: 3 :: Nil ) ) )
    }

    it should "be possible to combine Term with Policy validation raws" in {
        ( rule.required.validate( "foo" ).raw |@|
            ( rule.required && rule.min( 4 ) ).validate( "foo" ).raw ) map { ( _, _ ) } shouldBe
            Invalid( NonEmptyList( ( "min", "foo" :: 4 :: 3 :: Nil ) ) )

        ( ( rule.required && rule.min( 4 ) ).validate( "foo" ).raw |@|
            rule.required.validate( "" ).raw ) map { ( _, _ ) } shouldBe
            Invalid( NonEmptyList( ( "min", "foo" :: 4 :: 3 :: Nil ), ( "required", Nil ) ) )
    }

    //    it should "be possible to combine Term validations" in {
    //        ( rule.required.validate( "foo" ) |@|
    //            rule.required.validate( "" ) |@|
    //            rule.required.validate( "bar" ) ) map { ( _, _, _ ) } shouldBe
    //            Invalid( NonEmptyList( ( "required", Nil ) ) )
    //    }
    //
    //    it should "be possible to combine Policy validations" in {
    //        ( ( rule.required && rule.min( 4 ) ).validate( "foo" ).raw |@|
    //            ( rule.required && rule.max( 8 ) ).validate( "foobar" ).raw ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( ( "min", "foo" :: 4 :: 3 :: Nil ) ) )
    //    }
    //
    //    it should "be possible to combine Term with Policy validations" in {
    //        ( rule.required.validate( "foo" ).raw |@|
    //            ( rule.required && rule.min( 4 ) ).validate( "foo" ).raw ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( ( "min", "foo" :: 4 :: 3 :: Nil ) ) )
    //
    //        ( ( rule.required && rule.min( 4 ) ).validate( "foo" ).raw |@|
    //            rule.required.validate( "" ).raw ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( ( "min", "foo" :: 4 :: 3 :: Nil ), ( "required", Nil ) ) )
    //    }
    //
    //    it should "be possible to combine Term validation reports" in {
    //        import report._
    //
    //        ( rule.required.validate( "foo" ).report |@|
    //            rule.required.validate( "" ).report ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( "Pflichtfeld" ) )
    //    }
    //
    //    it should "be possible to combine Policy validation reports" in {
    //        import report._
    //
    //        ( ( rule.required && rule.min( 4 ) ).validate( "foo" ).report |@|
    //            ( rule.required && rule.max( 8 ) ).validate( "foobar" ).report ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( "Mindestens 4 Zeichen" ) )
    //    }
    //
    //    it should "be possible to combine Term with Policy validation reports" in {
    //        import report._
    //
    //        ( rule.required.validate( "foo" ).report |@|
    //            ( rule.required && rule.min( 4 ) ).validate( "foo" ).report ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( "Mindestens 4 Zeichen" ) )
    //
    //        ( ( rule.required && rule.min( 4 ) ).validate( "foo" ).report |@|
    //            rule.required.validate( "" ).report ) map { ( _, _ ) } shouldBe
    //            Invalid( NonEmptyList( "Mindestens 4 Zeichen", "Pflichtfeld" ) )
    //    }
}