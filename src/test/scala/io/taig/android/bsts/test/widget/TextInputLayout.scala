package io.taig.android.bsts.test.widget

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R
import io.taig.android.bsts.rule.string.Required
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers, RobolectricSuite}

@Config( sdk = Array( LOLLIPOP ) )
class	TextInputLayout
extends	FlatSpec
with	Matchers
with	BeforeAndAfterEach
with	RobolectricSuite
{
	implicit val context = RuntimeEnvironment.application

	var textInputLayout: android.support.design.widget.TextInputLayout = null

	var editText: android.widget.EditText = null

	override protected def beforeEach() =
	{
		textInputLayout = new android.support.design.widget.TextInputLayout( context )
		editText = new android.widget.EditText( context )
		textInputLayout.addView( editText )
	}

	"A TextInputLayout" should "be validatable" in
	{
		textInputLayout obeys Required()
		textInputLayout.validate() shouldBe false
		editText.setText( "asdf" )
		textInputLayout.validate() shouldBe true
	}

	it should "have an error message attached after failed validation" in
	{
		textInputLayout obeys Required()
		textInputLayout.validate()
		textInputLayout.getError.toString shouldBe context.getString( R.string.validation_string_required )
	}

	it should "not alter the underlying EditText's error message" in
	{
		textInputLayout obeys Required()
		textInputLayout.validate()
		editText.getError shouldBe null
	}
}