package test.io.taig.android.bsts.widget

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts._
import io.taig.android.bsts.default.show._
import io.taig.bsts.Show
import io.taig.bsts.rule._
import org.robolectric.annotation.Config
import org.scalatest.BeforeAndAfterEach
import test.io.taig.android.bsts.Suite

@Config( sdk = Array( LOLLIPOP ) )
class TextInputLayout
        extends Suite
        with BeforeAndAfterEach {
    var textInputLayout: android.support.design.widget.TextInputLayout = null

    var editText: android.widget.EditText = null

    override def beforeEach() = {
        textInputLayout = new android.support.design.widget.TextInputLayout( context )
        editText = new android.widget.EditText( context )
        textInputLayout.addView( editText )
    }

    it should "be validatable" in {
        textInputLayout obeys Required[String]()
        textInputLayout.validate() shouldBe false
        editText.setText( "asdf" )
        textInputLayout.validate() shouldBe true
    }

    it should "have an error message attached after failed validation" in {
        textInputLayout obeys Required[String]()
        textInputLayout.validate()
        textInputLayout.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )
    }

    it should "not alter the underlying EditText's error message" in {
        textInputLayout obeys Required[String]()
        textInputLayout.validate()
        editText.getError shouldBe null
    }
}