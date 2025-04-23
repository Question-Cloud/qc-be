package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.application.event.AbstractEventProcessor
import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentEventLogRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentEventLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsClient

@Component
class QuestionPaymentEventProcessor(
    private val questionPaymentEventLogRepository: QuestionPaymentEventLogRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsClient: SnsClient,
) : AbstractEventProcessor<QuestionPaymentEvent>() {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    override fun publishEvent(event: QuestionPaymentEvent) {
        snsClient.publish(event.toRequest())
        questionPaymentEventLogRepository.publish(event.questionPayment.order.orderId)
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

    override suspend fun republish(events: List<QuestionPaymentEvent>) {
        supervisorScope {
            events.forEach { event ->
                launch(Dispatchers.IO) {
                    snsClient.publish(event.toRequest())
                    questionPaymentEventLogRepository.publish(event.eventId)
                }
            }
        }
    }
}