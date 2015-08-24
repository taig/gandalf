package io.taig.android.bsts.test.rule.string

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts.{ Failure, Success }
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{ FlatSpec, Matchers, RobolectricSuite }

@Config( sdk = Array( LOLLIPOP ) )
class Required
        extends FlatSpec
        with Matchers
        with RobolectricSuite {
    import io.taig.android.bsts.rule.string.Required

    implicit val context = RuntimeEnvironment.application

    "A Required Rule" should "not accept an empty String" in {
        Required().validate( "" ) shouldBe a[Failure[_]]
    }

    it should "not get fooled by whitespace Strings" in {
        Required().validate( " " ) shouldBe a[Failure[_]]
        Required().validate( "    " ) shouldBe a[Failure[_]]
        Required().validate( "\t" ) shouldBe a[Failure[_]]
        Required().validate( "\n" ) shouldBe a[Failure[_]]
    }

    it should "accept non empty Strings" in {
        Required().validate( "a" ) shouldBe a[Success[_]]
        Required().validate( "asdf" ) shouldBe a[Success[_]]
        Required().validate( "1" ) shouldBe a[Success[_]]
        Required().validate( "1234" ) shouldBe a[Success[_]]
    }
}