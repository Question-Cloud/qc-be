package com.eager.questioncloud.event.implement

import com.eager.questioncloud.common.exception.ExceptionSlackNotifier
import com.eager.questioncloud.event.SQSEvent
import kotlinx.coroutines.future.await
import kotlinx.coroutines.supervisorScope
import org.springframework.scheduling.annotation.Scheduled
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.services.sns.model.PublishBatchRequest
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
        }.onFailure { e ->
            slackNotifier.sendApiException(e, "None", this.javaClass.name, this.javaClass.name)
        }
    }
    
    abstract fun getUnpublishedEvents(): List<T>
    
    private suspend fun republish(events: List<T>): List<String> {
        val publishedEventIds = CopyOnWriteArrayList<String>()
        supervisorScope {
            events.chunked(10).forEach { chunk ->
                val request = PublishBatchRequest.builder()
                    .topicArn(events.first().getTopicArn())
                    .publishBatchRequestEntries(chunk.mapIndexed { _, e -> e.toBatchRequestEntry() })
                    .build()
                
                val response = snsAsyncClient.publishBatch(request).await();
                
                response.successful().forEach {
                    publishedEventIds.add(it.id())
                }
            }
        }
        return publishedEventIds
    }
    
    abstract fun updateRepublishStatus(eventIds: List<String>)
}