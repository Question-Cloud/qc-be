package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.event.AbstractEventProcessor
import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewEventLogRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Component
class ReviewEventProcessor(
    private val questionReviewEventLogRepository: QuestionReviewEventLogRepository,
    private val snsAsyncClient: SnsAsyncClient,
    private val applicationEventPublisher: ApplicationEventPublisher
) : AbstractEventProcessor<ReviewEvent>() {
    
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

    override suspend fun republish(events: List<ReviewEvent>) {
        supervisorScope {
            events.forEach { event ->
                launch(Dispatchers.IO) {
                    snsAsyncClient.publish(event.toRequest()).await()
                    questionReviewEventLogRepository.publish(event.eventId)
                }
            }
        }
    }
}