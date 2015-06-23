package io.taig.android.bsts

sealed trait Result[T]
{
	def value: T
}

/**
 * Successful validation
 * 
 * @param value Value that passed validation
 */
case class	Success[T]( value: T )
extends		Result[T]

/**
 * Failed Validation
 * 
 * @param value Value that caused validation to fail
 */
case class	Failure[T]( value: T, errors: Seq[String] )
extends		Result[T]