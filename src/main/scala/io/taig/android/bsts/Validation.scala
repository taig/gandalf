package io.taig.android.bsts

/**
 * A Validation is a collection of Rules that is checked against the same value
 * 
 * Only if all rules pass, the value is considered valid
 * 
 * @param rules Rules to check
 */
case class Validation[T]( rules: Seq[Rule[T]] )
{
	def validate( value: T ): Result[T] =
	{
		val errors = rules
			.map( _.validate( value ) )
			.collect{ case f: Failure[T] => f.errors }
			.flatten
			.distinct

		errors match
		{
			case Nil => Success( value )
			case _ => Failure( value, errors )
		}
	}
}