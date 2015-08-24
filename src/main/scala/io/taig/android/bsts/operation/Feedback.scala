package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R

/**
 * Operations that enable a view to present validation feedback
 */
trait Feedback[V <: View] {
    def view: V

    /**
     * Show error messages in this view (or clear them with <code>None</code>)
     */
    def indicate( errors: Option[Seq[String]] )( implicit f: description.Feedback[V] ): Unit = {
        f.feedback( view, errors )
    }

    /**
     * Show error messages in this view
     */
    def indicate( errors: Seq[String] )( implicit f: description.Feedback[V] ): Unit = indicate( Some( errors ) )

    /**
     * Show an error message in this view
     */
    def indicate( error: String )( implicit f: description.Feedback[V] ): Unit = indicate( Seq( error ) )

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