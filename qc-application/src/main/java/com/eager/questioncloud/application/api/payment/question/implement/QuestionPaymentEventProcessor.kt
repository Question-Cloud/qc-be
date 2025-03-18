package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentEventLogRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentEventLog
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsClient
import java.time.LocalDateTime

@Component
class QuestionPaymentEventProcessor(
    private val questionPaymentEventLogRepository: QuestionPaymentEventLogRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsClient: SnsClient,
) {
    fun createEvent(questionPayment: QuestionPayment) {
        val questionPaymentEvent = QuestionPaymentEvent.create(questionPayment)
        questionPaymentEventLogRepository.save(
            QuestionPaymentEventLog(
                questionPayment.order.orderId,
                false,
                questionPaymentEvent.toJson(),
                LocalDateTime.now(),
            )
        )
        applicationEventPublisher.publishEvent(questionPaymentEvent)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishEvent(questionPaymentEvent: QuestionPaymentEvent) {
        snsClient.publish(questionPaymentEvent.toRequest())
        questionPaymentEventLogRepository.publish(questionPaymentEvent.questionPayment.order.orderId)
    }
}