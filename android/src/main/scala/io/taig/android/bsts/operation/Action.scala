package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.bsts.{ Failure, Success, Rule, Validation }
import shapeless.HList

/**
 * Operations that enable a view to run validation checks and clear error messages on itself and all its children
 *
 * Every view qualifies for these operations, but it's not guaranteed that the calls have any effect.
 */
abstract class Action[V <: View]( view: V ) {
    private val ruleChildren = view.children( _.getTag( R.id.bsts_rules ) != null )

    /**
     * Validate this view and all of its children, and update the Ui to show or hide error messages
     *
     * This method traverses the child views and checks for the R.id.bsts_rules tag.
     * If it finds a view it will execute the validation and update the Ui. Only if all children pass the validation,
     * this method will return <code>true</code>.
     */
    def validate(): Boolean = ruleChildren.flatMap( view ⇒ {
        type R = Rule

        val rules = view.getTag( R.id.bsts_rules ).asInstanceOf[Seq[operation.Runtime[R]]]
        val description = view.getTag( R.id.bsts_description ).asInstanceOf[Description[View, R#Value]]
        val value = description.data( view )

        rules.map( _.validate( value ) match {
            case Success( _ ) ⇒
                description.feedback( view, None )
                true
            case Failure( _, errors ) ⇒
                description.feedback( view, Some( errors ) )
                false
        } )
    } ).forall( _ == true )

    /**
     * Validate this view and all of its children (without propagating messages to the Ui)
     */
    def check(): Boolean = ruleChildren.flatMap( view ⇒ {
        type R = Rule

        val rules = view.getTag( R.id.bsts_rules ).asInstanceOf[Seq[operation.Runtime[R]]]
        val description = view.getTag( R.id.bsts_description ).asInstanceOf[Description[View, R#Value]]
        val value = description.data( view )

        rules.map( _.validate( value ).isSuccess )
    } ).forall( _ == true )

    /**
     * Remove error message of this view and all of its children
     */
    def clear(): Unit = ruleChildren.foreach( view ⇒ {
        view.getTag( R.id.bsts_description )
            .asInstanceOf[Description[View, _]]
            .feedback( view, None )
    } )

    /**
     * Search for views whose names match the map keys and then inject them if possible
     *
     * @return A map with the remaining keys that did not match any names
     */
    def indicate( errors: Map[String, Seq[String]] ): Map[String, Iterable[String]] = {
        val views = view
            .children( _.getTag( R.id.bsts_name ) != null )
            .map( view ⇒ view.getTag( R.id.bsts_name ).asInstanceOf[String] → view )
            .toMap

        val keys = views.keySet.intersect( errors.keySet )

        keys.foreach( key ⇒ {
            val view = views( key )

            view
                .getTag( R.id.bsts_description )
                .asInstanceOf[Description[View, _]]
                .feedback( view, errors.get( key ) )
        } )

        errors.filterKeys( !keys.contains( _ ) )
    }
}