package io.taig.android.bsts.operation

import android.view.{View, ViewGroup}
import io.taig.android.bsts._
import io.taig.android.bsts.description.Description
import io.taig.android.bsts.resource.R

/**
 * Operations that enable a view to run validation checks and clear error messages on itself and all its children
 * 
 * Every view qualifies for these operations
 */
trait Action[V <: View, T]
{
	def view: V

	/**
	 * Validate this view and all of its children, and update the Ui to show or hide error messages
	 *
	 * This method traverses the child views and checks for the R.id.validation_rules tag.
	 * If it finds a view it will execute the validation and update the Ui. Only if all children pass the validation,
	 * this method will return <code>true</code>.
	 */
	def validate(): Boolean =
	{
		children( view )
			.map( view =>
			{
				val rules = view.getTag( R.id.validation_rules ).asInstanceOf[Option[Seq[Rule[T]]]]
				val description = view.getTag( R.id.validation_description ).asInstanceOf[Description[View, T]]

				rules
					.map( Validation( _ ).validate( description.extract( view ) ) )
					.map
					{
						case Success( _ ) => description.feedback( view, None ); true
						case Failure( _, errors ) => description.feedback( view, Some( errors ) ); false
					}
					.getOrElse( true )
			} )
			.forall( _ == true )
	}

	/**
	 * Validate this view and all of its children (without propagating messages to the Ui)
	 */
	def check(): Boolean =
	{
		children( view ).forall( view =>
		{
			val rules = view.getTag( R.id.validation_rules ).asInstanceOf[Option[Seq[Rule[T]]]]

			rules
				.map( rules =>
				{
					val description = view.getTag( R.id.validation_description ).asInstanceOf[Description[View, T]]
					Validation( rules ).validate( description.extract( view ) ).isSuccess
				} )
				.getOrElse( true )
		} )
	}

	/**
	 * Remove error message of this view and all of its children
	 */
	def clear(): Unit =
	{
		children( view ).foreach( view =>
		{
			view.getTag( R.id.validation_description )
				.asInstanceOf[Description[View, T]]
				.feedback( view, None )
		} )
	}

	/**
	 * Recursively find all children that have rules attached
	 */
	private def children( view: View ): Seq[View] = view match
	{
		case _ if view.getTag( R.id.validation_rules ) != null => Seq( view )
		case viewGroup: ViewGroup =>
		{
			( 0 to viewGroup.getChildCount - 1 )
				.map( viewGroup.getChildAt )
				.flatMap( children )
		}
		case _ => Seq.empty
	}
}