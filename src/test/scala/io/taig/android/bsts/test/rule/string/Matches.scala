package io.taig.android.bsts.test.rule.string

import android.os.Build.VERSION_CODES.LOLLIPOP
import io.taig.android.bsts.{Failure, Success}
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.scalatest.{FlatSpec, Matchers, RobolectricSuite}

@Config( sdk = Array( LOLLIPOP ) )
class	Matches
extends	FlatSpec
with	Matchers
with	RobolectricSuite
{
	import io.taig.android.bsts.rule.string.Matches

	implicit val context = RuntimeEnvironment.application

	"An Matches Rule" should "accept an empty String" in
	{
		Matches( () => "" ).validate( "" ) shouldBe a [Success[_]]
	}

	it should "pass with equal inputs" in
	{
		val equal = "asdf"
		Matches( () => equal ).validate( equal ) shouldBe a [Success[_]]
	}

	it should "fail with unequal inputs" in
	{
		val equal = "asdf"
		Matches( () => equal ).validate( equal.reverse ) shouldBe a [Failure[_]]
	}

	it should "ignore whitespace" in
	{
		Matches( () => "\tasdf" ).validate( "   asdf  " ) shouldBe a [Success[_]]
	}
}