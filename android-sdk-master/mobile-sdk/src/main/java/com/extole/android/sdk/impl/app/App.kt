package com.extole.android.sdk.impl.app

import com.extole.android.sdk.impl.ExtoleInternal
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.LinkedList

object App {
    lateinit var extole: ExtoleInternal
    const val LOAD_DONE_EVENT = "load_done"

    private var appInitialized = false
    private val eventsQueue = LinkedList<AppEvent>()
    private val LOAD_EVENTS = listOf("on_load", "app_initialized")

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(appEvent: AppEvent?) {
        appEvent?.let { eventsQueue.add(appEvent) }
        if (appEvent?.eventName == LOAD_DONE_EVENT) {
            appInitialized = true
            extole.logger()
                .debug("App initialized, queued events ${eventsQueue.map { it.eventName }}")
        }
        while (eventCanBeProcessed(appEvent)) {
            val queuedEvent = eventsQueue.poll()
            runBlocking {
                extole.logger().debug("Received event $queuedEvent")
                if (queuedEvent != null) {
                    AppEngine(extole.operations()).execute(queuedEvent, extole)
                }
            }
        }
    }

    private fun eventCanBeProcessed(appEvent: AppEvent?) =
        (appInitialized || LOAD_EVENTS.contains(appEvent?.eventName)) && eventsQueue.isNotEmpty()
}
