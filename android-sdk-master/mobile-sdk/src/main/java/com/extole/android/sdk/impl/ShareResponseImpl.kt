package com.extole.android.sdk.impl

import com.extole.android.sdk.Event
import com.extole.android.sdk.Id
import com.extole.android.sdk.ShareResponse
import kotlinx.coroutines.Deferred

class ShareResponseImpl(
    private val partnerShareId: String,
    private val eventId: Deferred<Id<Event>>
) : ShareResponse {
    override fun partnerShareId(): String = partnerShareId

    override fun eventId(): Deferred<Id<Event>> = eventId
}
