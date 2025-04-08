package com.eager.questioncloud.application.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.scheduling.annotation.Scheduled

abstract class AbstractEventProcessor<T : SQSEvent> {
    val republishCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    abstract fun saveEventLog(event: T)

    abstract fun publishEvent(event: T)

    @Scheduled(fixedDelay = 10000)
    suspend fun republishScheduled() {
        var hasMoreEvents = true

        while (hasMoreEvents) {
            val events = getUnpublishedEvents()

            if (events.isEmpty()) {
                hasMoreEvents = false
            }

            republish(events)
        }
    }

    abstract fun getUnpublishedEvents(): List<T>

    abstract suspend fun republish(events: List<T>)
}