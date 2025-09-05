package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.exception.ExceptionSlackNotifier
import com.eager.questioncloud.event.SNSEvent
import com.eager.questioncloud.event.implement.AbstractEventProcessor
import com.eager.questioncloud.event.model.ReviewEvent
import com.eager.questioncloud.review.domain.QuestionReviewEventLog
import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewEventLogRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Component
class StoreReviewEventProcessor(
    private val questionReviewEventLogRepository: QuestionReviewEventLogRepository,
    private val snsAsyncClient: SnsAsyncClient,
    private val applicationEventPublisher: ApplicationEventPublisher,
    slackNotifier: ExceptionSlackNotifier,
) : AbstractEventProcessor<ReviewEvent>(snsAsyncClient, slackNotifier) {
    
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
            .map { log -> SNSEvent.objectMapper.readValue(log.payload, ReviewEvent::class.java) }
            .toList()
    }
    
    override fun updateRepublishStatus(eventIds: List<String>) {
        questionReviewEventLogRepository.publish(eventIds)
    }
}