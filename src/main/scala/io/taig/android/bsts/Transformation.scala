package io.taig.android.bsts

/**
 * Allow a rule to adjust incoming values before validating them
 * 
 * This might be useful to trim input, remove whitespaces from telephone numbers, etc.
 */
trait	Transformation[T]
extends	Rule[T]
{
	def transform( value: T ): T

	override def validate( value: T ): Result[T] = super.validate( transform( value ) )
}