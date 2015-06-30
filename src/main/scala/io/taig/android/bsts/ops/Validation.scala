package io.taig.android.bsts.ops

import android.view.{View, ViewGroup}
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R

/**
 * Every View may kick off a validation, checking itself and its children (if they were previously made Validatable)
 */
trait Validation[T]
{
	def view: View

	/**
	 * Validate this view and all of its children, and update the Ui to show or hide error messages
	 *
	 * This method traverses the child views and checks for the R.id.validation_hooking tag.
	 * If it finds a view it will execute the validation and update the Ui. Only if all children pass the validation,
	 * this method will return <code>true</code>.
	 */
	def validate(): Boolean =
	{
		children( view )
			.map( view =>
			{
				val ( rules, extraction, feedback ) = view
					.getTag( R.id.validation_hooking )
					.asInstanceOf[() => ( Seq[Rule[T]], Extraction[View, T], Feedback[View] )]
					.apply()

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
			} )
			.forall( _ == true )
	}

	/**
	 * Validate this view and all of its children
	 */
	def check(): Boolean =
	{
		children( view )
			.forall( view  =>
			{
				val ( rules, extraction, _ ) = view
					.getTag( R.id.validation_hooking )
					.asInstanceOf[() => ( Seq[Rule[T]], Extraction[View, T], Feedback[View] )]
					.apply()

				Validation( rules ).validate( extraction( view ) ) match
				{
					case Success( _ ) => true
					case Failure( _, _ ) => false
				}
			} )
	}

	/**
	 * Remove error message of this view and all of its children
	 */
	def clear(): Unit =
	{
		children( view )
			.foreach( view =>
			{
				val ( _, _, feedback ) = view
					.getTag( R.id.validation_hooking )
					.asInstanceOf[() => ( Seq[Rule[T]], Extraction[View, T], Feedback[View] )]
					.apply()

				feedback( view, None )
			} )
	}

	/**
	 * Recursively find all children that have rules attached
	 */
	private def children( view: View ): Seq[View] = view match
	{
		case _ if view.getTag( R.id.validation_hooking ) != null => Seq( view )
		case viewGroup: ViewGroup =>
		{
			( 0 to viewGroup.getChildCount - 1 )
				.map( viewGroup.getChildAt )
				.flatMap( children )
		}
		case _ => Seq.empty
	}
}