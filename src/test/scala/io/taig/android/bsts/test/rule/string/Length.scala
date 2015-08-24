package io.taig.android.bsts.test.rule.string

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts.{ Failure, Success }
import io.taig.android.bsts.rule.string._
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{ FlatSpec, Matchers, RobolectricSuite }

@Config( sdk = Array( LOLLIPOP ) )
class Length
        extends FlatSpec
        with Matchers
        with RobolectricSuite {

    implicit val context = RuntimeEnvironment.application

    "A Length Rule" should "accept an empty String" in {
        Exactly( 5 ).validate( "" ) shouldBe a[Success[_]]
        Min( 5 ).validate( "" ) shouldBe a[Success[_]]
        Max( 5 ).validate( "" ) shouldBe a[Success[_]]
    }

    it should "reject values that are too short" in {
        Min( 3 ).validate( "12" ) shouldBe a[Failure[_]]
    }

    it should "reject values that are too long" in {
        Max( 3 ).validate( "1234" ) shouldBe a[Failure[_]]
    }

    it should "reject values that do not have the exact length" in {
        Exactly( 3 ).validate( "12" ) shouldBe a[Failure[_]]
        Exactly( 3 ).validate( "1234" ) shouldBe a[Failure[_]]
    }
}