package io.taig.android.bsts

trait	Transformation[T]
extends	Rule[T]
{
	def transform( value: T ): T

	override def validate( value: T ): Result[T] = super.validate( transform( value ) )
}