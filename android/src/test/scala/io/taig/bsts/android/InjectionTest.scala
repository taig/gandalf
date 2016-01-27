package io.taig.bsts.android

import android.os.Build.VERSION_CODES.LOLLIPOP
import android.view.{ ViewGroup, View }
import android.widget.AdapterView.INVALID_POSITION
import android.widget._
import org.robolectric.annotation.Config
import io.taig.bsts.android.syntax.value._

@Config( sdk = Array( LOLLIPOP ) )
class InjectionTest extends Suite {
    it should "inject Boolean into CompoundButton" in {
        val view = new Switch( context )
        view.value = true
        view.isChecked shouldBe true
        view.value = false
        view.isChecked shouldBe false
    }

    it should "inject Int into RadioGroup" in {
        val view = new RadioGroup( context )
        view.value = 3
        view.getCheckedRadioButtonId shouldBe 3
    }

    it should "inject Option[Int] into RadioGroup" in {
        val view = new RadioGroup( context )
        view.value = Some( 3 )
        view.getCheckedRadioButtonId shouldBe 3
        view.value = None
        view.getCheckedRadioButtonId shouldBe INVALID_POSITION
    }

    it should "inject CharSequence into TextView" in {
        val view = new TextView( context )
        view.value = "foobar"
        view.getText.toString shouldBe "foobar"
    }

    it should "inject Option[CharSequence] into TextView" in {
        val view = new TextView( context )
        view.value = Some( "foobar" )
        view.getText.toString shouldBe "foobar"
        view.value = None
        view.getText.toString shouldBe ""
    }
}