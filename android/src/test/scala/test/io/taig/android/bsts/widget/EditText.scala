package test.io.taig.android.bsts.widget

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts._
import io.taig.android.bsts.default.show._
import io.taig.android.bsts.resource.R
import io.taig.bsts.Show
import io.taig.bsts.rule._
import org.robolectric.annotation.Config
import org.scalatest.BeforeAndAfterEach
import test.io.taig.android.bsts.Suite

@Config( sdk = Array( LOLLIPOP ) )
class EditText
        extends Suite
        with BeforeAndAfterEach {
    var editText: android.widget.EditText = null

    override def beforeEach() = {
        editText = new android.widget.EditText( context )
    }

    it should "be validatable" in {
        editText obeys Required[String]()
        editText.validate() shouldBe false
        editText.setText( "asdf" )
        editText.validate() shouldBe true
    }

    it should "have an error message attached after failed validation" in {
        editText obeys Required[String]()
        editText.validate()
        editText.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )
    }

    it should "attach the error message to a wrapping TextInputLayout" in {
        val wrapper = new android.support.design.widget.TextInputLayout( context )
        wrapper.addView( editText )
        editText obeys Required[String]()
        editText.validate()
        editText.getError shouldBe null
        editText.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )
        wrapper.indication shouldBe Some( Seq( context.getString( R.string.bsts_required ) ) )
    }
}