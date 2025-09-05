package com.eager.questioncloud.pay.question.implement

import com.eager.questioncloud.common.exception.ExceptionSlackNotifier
import com.eager.questioncloud.event.SNSEvent
import com.eager.questioncloud.event.implement.AbstractEventProcessor
import com.eager.questioncloud.event.model.QuestionPaymentEvent
import com.eager.questioncloud.payment.domain.QuestionPaymentEventLog
import com.eager.questioncloud.payment.infrastructure.repository.QuestionPaymentEventLogRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Component
class QuestionPaymentEventProcessor(
    private val questionPaymentEventLogRepository: QuestionPaymentEventLogRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsAsyncClient: SnsAsyncClient,
    private val slackNotifier: ExceptionSlackNotifier
) : AbstractEventProcessor<QuestionPaymentEvent>(snsAsyncClient, slackNotifier) {
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    override fun publishEvent(event: QuestionPaymentEvent) {
        snsAsyncClient.publish(event.toRequest())
            .thenAcceptAsync {
                questionPaymentEventLogRepository.publish(event.data.orderId)
            }
    }
    
    override fun saveEventLog(event: QuestionPaymentEvent) {
        questionPaymentEventLogRepository.save(QuestionPaymentEventLog.create(event.eventId, event.toJson()))
        applicationEventPublisher.publishEvent(event)
    }
    
    override fun getUnpublishedEvents(): List<QuestionPaymentEvent> {
        return questionPaymentEventLogRepository.getUnPublishedEvent()
            .stream()
            .map { log -> SNSEvent.objectMapper.readValue(log.payload, QuestionPaymentEvent::class.java) }
            .toList()
    }
    
    override fun updateRepublishStatus(eventIds: List<String>) {
        questionPaymentEventLogRepository.publish(eventIds)
    }
}