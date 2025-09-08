package com.eager.questioncloud.event.implement

import com.eager.questioncloud.event.SNSEvent
import com.eager.questioncloud.event.repository.EventTicketRepository
import com.eager.questioncloud.event.model.EventTicket
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Component
class EventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsAsyncClient: SnsAsyncClient,
    private val objectMapper: ObjectMapper,
    private val eventTicketRepository: EventTicketRepository
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishEvent(event: SNSEvent) {
        snsAsyncClient.publish(event.toRequest(objectMapper))
            .thenAcceptAsync {
                eventTicketRepository.publish(event.eventId)
            }
    }
    
    fun saveEventTicket(event: SNSEvent) {
        eventTicketRepository.save(EventTicket.create(event, objectMapper))
        applicationEventPublisher.publishEvent(event)
    }
}