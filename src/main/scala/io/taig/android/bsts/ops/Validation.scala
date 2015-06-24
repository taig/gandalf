package io.taig.android.bsts.ops

import android.view.{View, ViewGroup}
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R

/**
 * Every View may kick off a validation, checking itself and its children (if they were previously made Validatable)
 */
trait Validation[V <: View, T]
{
	def view: V

	/**
	 * Validate this view and all of his children
	 *
	 * This method traverses the child views and checks for the R.id.validation_hooking tag.
	 * If it finds a view it will execute the validation. Only if all children pass the validation, this method will
	 * return <code>true</code>.
	 */
	def validate(): Boolean =
	{
		val extract = view
			.getTag( R.id.validation_hooking )
			.asInstanceOf[V => ( Seq[Rule[T]], Extraction[V, T], Feedback[V] )]

		view match
		{
			case _ if extract != null =>
			{
				val ( rules, extraction, feedback ) = extract( view )

				Validation( rules ).validate( extraction( view ) ) match
				{
					case Success( _ ) =>
					{
						feedback( view, None )
						true
					}
					case Failure( _, errors ) =>
					{
						feedback( view, Some( errors ) )
						false
					}
				}
			}
			case viewGroup: ViewGroup =>
			{
				children( viewGroup )
					// Don't do .forall( _.validate() == true ), because it will stop on the first false and will
					// therefore not propagate all errors to the Ui, but only the first one
					.map( _.validate() )
					.forall( _ == true )
			}
			case _ => true
		}
	}

	/**
	 * Recursively find all children that have rules attached
	 */
	private def children( view: ViewGroup ) =
	{
		def discover( view: ViewGroup ): Seq[View] =
		{
			( 0 to view.getChildCount - 1 )
				.map( view.getChildAt )
				.collect
				{
					case validatable if validatable.getTag( R.id.validation_rules ) != null => Seq( validatable )
					case group: ViewGroup => discover( group )
				}
				.flatten
		}

		discover( view )
	}
}