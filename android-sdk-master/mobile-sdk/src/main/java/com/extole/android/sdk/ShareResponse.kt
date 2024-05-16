package com.extole.android.sdk

import kotlinx.coroutines.Deferred

interface ShareResponse {

    fun partnerShareId(): String
    fun eventId(): Deferred<Id<Event>>
}
