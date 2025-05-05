package com.eager.questioncloud.application.event

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.scheduling.annotation.Scheduled
import software.amazon.awssdk.services.sns.SnsAsyncClient

abstract class AbstractEventProcessor<T : SQSEvent>(
    private val snsAsyncClient: SnsAsyncClient
) {
    abstract fun saveEventLog(event: T)

    abstract fun publishEvent(event: T)

    @Scheduled(fixedDelay = 10000)
    open suspend fun republishScheduled() {
        var hasMoreEvents = true

        while (hasMoreEvents) {
            val events = getUnpublishedEvents()

            if (events.isEmpty()) {
                hasMoreEvents = false
            }

            val publishedEventIds = republish(events)
            updateRepublishStatus(publishedEventIds)
        }
    }

    abstract fun getUnpublishedEvents(): List<T>

    private suspend fun republish(eventIds: List<T>): List<String> {
        val publishedEventIds = mutableListOf<String>()
        supervisorScope {
            eventIds.forEach { event ->
                launch(Dispatchers.IO) {
                    snsAsyncClient.publish(event.toRequest()).await()
                    publishedEventIds.add(event.eventId)
                }
            }
        }
        return publishedEventIds
    }

    abstract fun updateRepublishStatus(eventIds: List<String>)
}