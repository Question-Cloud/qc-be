package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.event.AbstractEventProcessor
import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewEventLogRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Component
class ReviewEventProcessor(
    private val questionReviewEventLogRepository: QuestionReviewEventLogRepository,
    private val snsAsyncClient: SnsAsyncClient,
    private val applicationEventPublisher: ApplicationEventPublisher
) : AbstractEventProcessor<ReviewEvent>(snsAsyncClient) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    override fun publishEvent(event: ReviewEvent) {
        snsAsyncClient.publish(event.toRequest())
            .thenAcceptAsync {
                questionReviewEventLogRepository.publish(event.eventId)
            }
    }

    override fun saveEventLog(event: ReviewEvent) {
        questionReviewEventLogRepository.save(QuestionReviewEventLog.create(event.eventId, event.toJson()))
        applicationEventPublisher.publishEvent(event)
    }

    override fun getUnpublishedEvents(): List<ReviewEvent> {
        return questionReviewEventLogRepository.getUnPublishedEvent()
            .stream()
            .map { log -> SQSEvent.objectMapper.readValue(log.payload, ReviewEvent::class.java) }
            .toList()
    }

    override fun updateRepublishStatus(eventIds: List<String>) {
        questionReviewEventLogRepository.publish(eventIds)
    }
}