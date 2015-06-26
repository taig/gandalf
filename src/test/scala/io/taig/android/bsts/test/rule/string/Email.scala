package io.taig.android.bsts.test.rule.string

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts.{Failure, Success}
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{FlatSpec, Matchers, RobolectricSuite}

@Config( sdk = Array( LOLLIPOP ) )
class	Email
extends	FlatSpec
with	Matchers
with	RobolectricSuite
{
	import io.taig.android.bsts.rule.string.Email

	implicit val context = RuntimeEnvironment.application

	"An Email Rule" should "accept an empty String" in
	{
		Email().validate( "" ) shouldBe a [Success[_]]
	}

	it should "accept valid email addresses" in
	{
		Email().validate( "mail@taig.io" ) shouldBe a [Success[_]]
		Email().validate( "mail+bsts@taig.io" ) shouldBe a [Success[_]]
		Email().validate( "mail123.321liam@taig.io" ) shouldBe a [Success[_]]
	}

	it should "should fail on malformed address formats" in
	{
		Email().validate( "mail@taig" ) shouldBe a [Failure[_]]
		Email().validate( "@taig.io" ) shouldBe a [Failure[_]]
	}

	it should "ignore outer whitespace" in
	{
		Email().validate( " mail@taig.io " ) shouldBe a [Success[_]]
		Email().validate( " mail@taig.io" ) shouldBe a [Success[_]]
		Email().validate( "mail@taig.io " ) shouldBe a [Success[_]]
	}
}