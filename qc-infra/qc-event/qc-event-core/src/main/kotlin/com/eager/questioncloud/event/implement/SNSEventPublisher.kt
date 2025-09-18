package com.eager.questioncloud.event.implement

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.event.TopicArn
import com.eager.questioncloud.event.model.EventTicket
import com.eager.questioncloud.event.repository.EventTicketRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.services.sns.model.PublishRequest

@Component
class SNSEventPublisher(
    private val objectMapper: ObjectMapper,
    private val eventTicketRepository: EventTicketRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsAsyncClient: SnsAsyncClient
) : EventPublisher {
    fun Event.toPublishRequest(): PublishRequest {
        return PublishRequest.builder()
            .topicArn(TopicArn.valueOf(eventType.name).topicArn)
            .messageGroupId(eventId)
            .messageDeduplicationId(eventId)
            .message(objectMapper.writeValueAsString(this))
            .build()
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    override fun publish(event: Event) {
        eventTicketRepository.save(EventTicket.create(event, objectMapper))
        applicationEventPublisher.publishEvent(event)
    }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishSNS(event: Event) {
        snsAsyncClient.publish(event.toPublishRequest())
            .thenAcceptAsync {
                eventTicketRepository.publish(event.eventId)
            }
    }
}