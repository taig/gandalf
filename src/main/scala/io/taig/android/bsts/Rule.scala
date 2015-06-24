package io.taig.android.bsts

import android.content.Context

trait Rule[T]
{
	implicit def context: Context

	/**
	 * The actual rule logic
	 * 
	 * @param value Value to validate
	 * @return <code>true</code> if validation passes, <code>false</code> otherwise
	 */
	protected def apply( value: T ): Boolean

	def validate( value: T ): Result[T] =
	{
		apply( value ) match
		{
			case true => Success( value )
			case failures => Failure( value, Seq( message( value ) ) )
		}
	}

	def message( value: T ): String
}