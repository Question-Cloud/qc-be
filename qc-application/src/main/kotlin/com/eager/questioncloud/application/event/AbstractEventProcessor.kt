package com.eager.questioncloud.application.event

import com.eager.questioncloud.application.exception.ExceptionSlackNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.scheduling.annotation.Scheduled
import software.amazon.awssdk.services.sns.SnsAsyncClient
import java.util.concurrent.CopyOnWriteArrayList

abstract class AbstractEventProcessor<T : SQSEvent>(
    private val snsAsyncClient: SnsAsyncClient,
    private val slackNotifier: ExceptionSlackNotifier
) {
    abstract fun saveEventLog(event: T)

    abstract fun publishEvent(event: T)

    @Scheduled(fixedDelay = 10000)
    open suspend fun republishScheduled() {
        runCatching {
            var hasMoreEvents = true

            while (hasMoreEvents) {
                val events = getUnpublishedEvents()

                if (events.isEmpty()) {
                    hasMoreEvents = false
                }

                val publishedEventIds = republish(events)
                updateRepublishStatus(publishedEventIds)
            }
            throw RuntimeException("스케줄러 예외 발생!!")
        }.onFailure { e ->
            slackNotifier.sendApiException(e, "None", this.javaClass.name, this.javaClass.name)
        }
    }

    abstract fun getUnpublishedEvents(): List<T>

    private suspend fun republish(eventIds: List<T>): List<String> {
        val publishedEventIds = CopyOnWriteArrayList<String>()
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