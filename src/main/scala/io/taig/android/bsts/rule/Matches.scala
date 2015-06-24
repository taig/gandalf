package io.taig.android.bsts.rule

import io.taig.android.bsts.{Transformation, R, Rule}

trait	Matches[T]
extends	Rule[T]
with	Transformation[T]
{
	/**
	 * The name of the target, used to build the message ("Does not match target")
	 */
	def target: Option[String]

	/**
	 * The target value to check against
	 */
	def value: () => T

	override def transform( value: T ) = value

	override protected def apply( value: T ): Boolean = transform( this.value() ) == value

	override def message( value: T ) = target match
	{
		case Some( target ) => context.getString( R.string.validation_matches_target, target )
		case None => context.getString( R.string.validation_matches )
	}
}