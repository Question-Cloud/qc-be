package com.eager.questioncloud.event.implement

import com.eager.questioncloud.event.repository.EventTicketRepository
import com.eager.questioncloud.event.model.EventTicket
import kotlinx.coroutines.future.await
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.services.sns.model.PublishBatchRequest
import software.amazon.awssdk.services.sns.model.PublishBatchRequestEntry
import java.util.concurrent.CopyOnWriteArrayList

@Component
class EventRePublisher(
    private val eventTicketRepository: EventTicketRepository,
    private val snsAsyncClient: SnsAsyncClient,
) {
    @Scheduled(fixedRate = 10000)
    suspend fun republish() {
        while (true) {
            val unPublishTickets = eventTicketRepository.getUnPublishEventTickets()
            val ticketsMap: Map<String, List<EventTicket>> = unPublishTickets.groupBy { it.eventType }
            
            if (unPublishTickets.isEmpty()) {
                break
            }
            
            val publishedEventIds = CopyOnWriteArrayList<String>()
            
            for ((eventType, tickets) in ticketsMap) {
                tickets.chunked(10).forEach { chunk ->
                    val request = PublishBatchRequest.builder()
                        .topicArn(eventType)
                        .publishBatchRequestEntries(chunk.mapIndexed { _, e -> toBatchRequestEntry(e) })
                        .build()
                    
                    val response = snsAsyncClient.publishBatch(request).await();
                    
                    response.successful().forEach {
                        publishedEventIds.add(it.id())
                    }
                }
            }
            
            eventTicketRepository.publish(publishedEventIds)
        }
    }
    
    private fun toBatchRequestEntry(ticket: EventTicket): PublishBatchRequestEntry {
        return PublishBatchRequestEntry.builder()
            .id(ticket.eventId)
            .messageDeduplicationId(ticket.eventId)
            .messageGroupId(ticket.eventId)
            .message(ticket.payload)
            .build()
    }
}