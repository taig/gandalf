package io.taig.bsts.android.ops

import android.view.{ View, ViewGroup }
import cats.data.{ NonEmptyList, Validated, OneAnd }
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.android.syntax.tags._

final class view[V <: View]( view: V ) {
    /**
     * Validate this view and all of its children, and update the Ui to show or hide error messages
     *
     * This method traverses the child views and checks for the R.id.bsts_validation tag.
     * If it finds a view it will execute the validation and update the Ui. Only if all children pass the validation,
     * this method will return <code>true</code>.
     */
    def validateAll: Validated[NonEmptyList[String], List[Any]] = {
        import cats.syntax.cartesian._
        import cats.std.all._

        rules
            .map( new view( _ ).validate[Any] )
            .foldLeft[Validated[NonEmptyList[String], List[Any]]]( Valid( Nil ) ) { ( a, b ) ⇒
                ( a |@| b ) map { _ :+ _ }
            }
    }

    def validate[O]: Validated[NonEmptyList[String], O] = view.validation[O]() match {
        case valid @ Valid( _ ) ⇒
            view.feedback.set( view, None )
            valid
        case invalid @ Invalid( OneAnd( error, _ ) ) ⇒
            view.feedback.set( view, Some( error ) )
            invalid
    }

    /**
     * Validate this view and all of its children (without propagating messages to the Ui)
     */
    def check(): Boolean = rules.forall( _.validation().isValid )

    /**
     * Remove error message of this view and all of its children
     */
    def clear(): Unit = rules.foreach( _.feedback.set( view, None ) )

    def name( name: String ): V = {
        view.name = name
        view
    }

    /**
     * Search for views whose names match the map keys and then inject them if possible
     *
     * @return A map with the remaining keys that did not match any names
     */
    def indicate( errors: Map[String, Seq[String]] ): Map[String, Iterable[String]] = {
        val views = children( _.name != null )
            .map( view ⇒ view.name → view )
            .toMap

        val keys = views.keySet.intersect( errors.keySet )

        keys.foreach { key ⇒
            val view = views( key )

            view
                .feedback
                .set( view, errors.get( key ).flatMap( _.headOption ) )
        }

        errors.filterKeys( !keys.contains( _ ) )
    }

    private val rules: Seq[View] = children( _.validation != null )

    /**
     * Recursively find all children that fulfill the given requirement
     */
    private[android] def children( requirement: View ⇒ Boolean ): Seq[View] = children( view, requirement )

    private def children( view: View, requirement: View ⇒ Boolean ): Seq[View] = view match {
        case _ if requirement( view ) ⇒ Seq( view )
        case viewGroup: ViewGroup ⇒
            ( 0 until viewGroup.getChildCount )
                .map( viewGroup.getChildAt )
                .flatMap( children( _, requirement ) )
        case _ ⇒ Seq.empty
    }
}