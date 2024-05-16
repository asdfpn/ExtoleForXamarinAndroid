package com.extole.android.sdk.impl.app

import com.extole.android.sdk.Action
import com.extole.android.sdk.Condition
import com.extole.android.sdk.Operation
import com.extole.android.sdk.impl.ExtoleInternal

class OperationImpl(
    private val conditions: List<Condition>,
    private val actions: List<Action>
) : Operation {

    override suspend fun executeActions(event: AppEvent, extole: ExtoleInternal) {
        actionsToExecute(event, extole).forEach {
            if (!extole.disabledActions().contains(it.getType())) {
                extole.logger().debug("Executing action ${it.getTitle()}")
                it.execute(event, extole)
            } else {
                extole.logger().debug("Skipping ${it.getType()} because it is disabled")
            }
        }
    }

    override fun passingConditions(
        event: AppEvent,
        extole: ExtoleInternal
    ): List<Condition> {
        return conditions.filter { it.passes(event, extole) }
    }

    override fun actionsToExecute(event: AppEvent, extole: ExtoleInternal): List<Action> {
        if (passingConditions(event, extole).size == conditions.size) {
            return actions
        }
        return emptyList()
    }

    override fun actions(): List<Action> = actions

    override fun conditions(): List<Condition> = conditions

    override fun toString(): String {
        return "Operation Conditions: $conditions, Actions: $actions"
    }
}
