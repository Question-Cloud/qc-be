package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.event.AbstractEventProcessor
import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.application.exception.ExceptionSlackNotifier
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentEventLogRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentEventLog
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
                questionPaymentEventLogRepository.publish(event.questionPayment.order.orderId)
            }
    }

    override fun saveEventLog(event: QuestionPaymentEvent) {
        questionPaymentEventLogRepository.save(QuestionPaymentEventLog.create(event.eventId, event.toJson()))
        applicationEventPublisher.publishEvent(event)
    }

    override fun getUnpublishedEvents(): List<QuestionPaymentEvent> {
        return questionPaymentEventLogRepository.getUnPublishedEvent()
            .stream()
            .map { log -> SQSEvent.objectMapper.readValue(log.payload, QuestionPaymentEvent::class.java) }
            .toList()
    }

    override fun updateRepublishStatus(eventIds: List<String>) {
        questionPaymentEventLogRepository.publish(eventIds)
    }
}