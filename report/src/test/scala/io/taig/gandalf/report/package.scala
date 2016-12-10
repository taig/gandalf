package io.taig.gandalf

import io.taig.gandalf.core.condition

package object report {
    implicit def reportConditionFailure[I, O] =
        Report.static[condition.failure, I, O]( "Fehlschlag" )
}