package com.eager.questioncloud.event.scheduler

import com.eager.questioncloud.event.repository.EventTicketRepository
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sns.SnsAsyncClient
import java.util.concurrent.CopyOnWriteArrayList

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
            
            val sentIds = CopyOnWriteArrayList<String>()
            
            supervisorScope {
                for (ticket in tickets) {
                    launch {
                        snsAsyncClient.publish(ticket.toPublishRequest()).await()
                        sentIds.add(ticket.eventId)
                    }
                }
            }
            
            eventTicketRepository.publish(sentIds)
        }
    }
}