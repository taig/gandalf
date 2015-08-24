package io.taig.android.bsts.operation

import android.view.{ View, ViewGroup }

trait Search {
    def view: View

    /**
     * Recursively find all children that fulfill the requirement
     */
    def children( requirement: View ⇒ Boolean ): Seq[View] = children( view, requirement )

    private def children( view: View, requirement: View ⇒ Boolean ): Seq[View] = view match {
        case view if requirement( view ) ⇒ Seq( view )
        case viewGroup: ViewGroup ⇒ {
            ( 0 to viewGroup.getChildCount - 1 )
                .map( viewGroup.getChildAt )
                .flatMap( children( _, requirement ) )
        }
        case _ ⇒ Seq.empty
    }
}