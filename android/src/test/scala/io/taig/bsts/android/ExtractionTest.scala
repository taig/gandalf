package io.taig.bsts.android

import android.os.Build.VERSION_CODES.LOLLIPOP
import android.widget._
import io.taig.bsts.android.syntax.value._
import org.robolectric.annotation.Config

@Config( sdk = Array( LOLLIPOP ) )
class ExtractionTest extends Suite {
    it should "extract Boolean from CompoundButton" in {
        val view = new Switch( context )
        view.setChecked( true )
        view.value[Boolean] shouldBe true
        view.setChecked( false )
        view.value[Boolean] shouldBe false
    }

    it should "extract Int from RadioGroup" in {
        val view = new RadioGroup( context )
        view.check( 3 )
        view.value[Int] shouldBe 3
    }

    it should "extract Option[Int] from RadioGroup" in {
        val view = new RadioGroup( context )
        view.check( 3 )
        view.value[Option[Int]] shouldBe Some( 3 )
        view.clearCheck()
        view.value[Option[Int]] shouldBe None
    }

    it should "extract CharSequence from TextView" in {
        val view = new TextView( context )
        view.setText( "foobar" )
        view.value[String] shouldBe "foobar"
    }

    it should "extract Option[CharSequence] from TextView" in {
        val view = new TextView( context )
        view.setText( "foobar" )
        view.value[Option[String]] shouldBe Some( "foobar" )
        view.setText( null )
        view.value[Option[String]] shouldBe None
    }
}