package io.taig.android.bsts.test.widget

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R
import io.taig.android.bsts.rule.string.Required
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers, RobolectricSuite}

@Config( sdk = Array( LOLLIPOP ), manifest = "src/main/AndroidManifest.xml" )
class	EditText
extends	FlatSpec
with	Matchers
with	BeforeAndAfterEach
with	RobolectricSuite
{
	import android.support.design.widget.TextInputLayout

	implicit val context = RuntimeEnvironment.application

	var editText: android.widget.EditText = null

	override protected def beforeEach() =
	{
		editText = new android.widget.EditText( context )
	}

	"An EditText" should "be validatable" in
	{
		editText withRule Required()
		editText.validate() shouldBe false
		editText.setText( "asdf" )
		editText.validate() shouldBe true
	}

	it should "have an error message attached after failed validation" in
	{
		editText withRule Required()
		editText.validate()
		editText.getError.toString shouldBe context.getString( R.string.validation_string_required )
	}

	it should "attach the error message to a wrapping TextInputLayout" in
	{
		val wrapper = new TextInputLayout( context )
		wrapper.addView( editText )
		editText withRule Required()
		editText.validate()
		editText.getError shouldBe null
		wrapper.getError shouldBe context.getString( R.string.validation_string_required )
	}
}