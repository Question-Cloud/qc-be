package com.eager.questioncloud.event.scheduler

import com.eager.questioncloud.event.repository.EventTicketRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.future.await
import kotlinx.coroutines.supervisorScope
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Profile("prod", "local")
@Component
class EventTicketRepublishScheduler(
    private val eventTicketRepository: EventTicketRepository,
    private val snsAsyncClient: SnsAsyncClient
) {
    @Scheduled(fixedRate = 5000)
    suspend fun republish() {
        while (true) {
            val tickets = eventTicketRepository.getUnPublishEventTickets();
            if (tickets.isEmpty()) break
            
            val sentIds = supervisorScope {
                tickets.map { ticket ->
                    async {
                        snsAsyncClient.publish(ticket.toPublishRequest()).await()
                        ticket.eventId
                    }
                }
            }.awaitAll()
            
            eventTicketRepository.publish(sentIds)
        }
    }
}