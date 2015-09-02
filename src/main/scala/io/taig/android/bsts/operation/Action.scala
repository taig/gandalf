package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.description.Description
import io.taig.android.bsts.resource.R

/**
 * Operations that enable a view to run validation checks and clear error messages on itself and all its children
 *
 * Every view qualifies for these operations
 */
trait Action[V <: View, T] {
    def view: V

    private val ruleChildren = view.children( _.getTag( R.id.validation_rules ) != null )

    /**
     * Validate this view and all of its children, and update the Ui to show or hide error messages
     *
     * This method traverses the child views and checks for the R.id.validation_rules tag.
     * If it finds a view it will execute the validation and update the Ui. Only if all children pass the validation,
     * this method will return <code>true</code>.
     */
    def validate(): Boolean = {
        ruleChildren
            .map( view ⇒ {
                val rules = view.getTag( R.id.validation_rules ).asInstanceOf[Option[Seq[Rule[T]]]]
                val description = view.getTag( R.id.validation_description ).asInstanceOf[Description[View, T]]

                rules
                    .map( Validation( _ ).validate( description.extract( view ) ) )
                    .map {
                        case Success( _ ) ⇒
                            description.feedback( view, None )
                            true
                        case Failure( _, errors ) ⇒
                            description.feedback( view, Some( errors ) )
                            false
                    }
                    .getOrElse( true )
            } )
            .forall( _ == true )
    }

    /**
     * Validate this view and all of its children (without propagating messages to the Ui)
     */
    def check(): Boolean = {
        ruleChildren.forall( view ⇒ {
            val rules = view.getTag( R.id.validation_rules ).asInstanceOf[Option[Seq[Rule[T]]]]

            rules
                .map( rules ⇒ {
                    val description = view.getTag( R.id.validation_description ).asInstanceOf[Description[View, T]]
                    Validation( rules ).validate( description.extract( view ) ).isSuccess
                } )
                .getOrElse( true )
        } )
    }

    /**
     * Remove error message of this view and all of its children
     */
    def clear(): Unit = {
        ruleChildren.foreach( view ⇒ {
            view.getTag( R.id.validation_description )
                .asInstanceOf[Description[View, T]]
                .feedback( view, None )
        } )
    }

    /**
     * Search for views whose names match the map keys and then inject them if possible
     *
     * @return A map with the remaining keys that did not match any names
     */
    def indicate( errors: Map[String, Seq[String]] ): Map[String, Iterable[String]] = {
        val views = view
            .children( _.getTag( R.id.view_name ) != null )
            .map( view ⇒ view.getTag( R.id.view_name ).asInstanceOf[String] → view )
            .toMap

        val keys = views.keySet.intersect( errors.keySet )

        keys.foreach( key ⇒ {
            val view = views( key )
            val feedback = view
                .getTag( R.id.validation_description )
                .asInstanceOf[description.Feedback[View]]

            feedback.feedback( view, errors.get( key ) )
        } )

        errors.filterKeys( !keys.contains( _ ) )
    }
}