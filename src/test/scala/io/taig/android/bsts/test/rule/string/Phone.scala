package io.taig.android.bsts.test.rule.string

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts.{Failure, Success}
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{FlatSpec, Matchers, RobolectricSuite}

@Config( sdk = Array( LOLLIPOP ) )
class	Phone
extends	FlatSpec
with	Matchers
with	RobolectricSuite
{
	import io.taig.android.bsts.rule.string.Phone

	implicit val context = RuntimeEnvironment.application

	"A Phone Rule" should "accept an empty String" in
	{
		Phone().validate( "" ) shouldBe a [Success[_]]
	}

	it should "accept valid phone numbers" in
	{
		Phone().validate( "110" ) shouldBe a [Success[_]]
		Phone().validate( "030123456" ) shouldBe a [Success[_]]
	}

	it should "accept numbers with leading + country code" in
	{
		Phone().validate( "+49110" ) shouldBe a [Success[_]]
		Phone().validate( "+4930123456" ) shouldBe a [Success[_]]
	}

	it should "accept formatted numbers" in
	{
		Phone().validate( "+49 110" ) shouldBe a [Success[_]]
		Phone().validate( "+49 30 123 456" ) shouldBe a [Success[_]]
		Phone().validate( "+49-30-123-456" ) shouldBe a [Success[_]]
	}

	it should "fail on malformed numbers" in
	{
		Phone().validate( "+49 +110" ) shouldBe a [Failure[_]]
		Phone().validate( "+49 30 asdf" ) shouldBe a [Failure[_]]
	}
}