package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.application.event.SQSEvent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewEventLogRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog
import io.hypersistence.tsid.TSID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsClient
import java.time.LocalDateTime

@Component
class ReviewEventProcessor(
    private val questionReviewEventLogRepository: QuestionReviewEventLogRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsClient: SnsClient
) {
    private val republishCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun createEvent(questionId: Long, varianceRate: Int, reviewEventType: ReviewEventType) {
        val reviewEvent = ReviewEvent(TSID.Factory.getTsid().toString(), questionId, varianceRate, reviewEventType)
        questionReviewEventLogRepository.save(
            QuestionReviewEventLog(
                reviewEvent.eventId,
                false,
                reviewEvent.toJson(),
                LocalDateTime.now(),
            )
        )
        applicationEventPublisher.publishEvent(reviewEvent)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishEvent(reviewEvent: ReviewEvent) {
        snsClient.publish(reviewEvent.toRequest())
        questionReviewEventLogRepository.publish(reviewEvent.eventId)
    }

    @Scheduled(fixedDelay = 10000)
    suspend fun republishScheduled() {
        var hasMoreEvents = true
        while (hasMoreEvents) {
            val events = getUnpublishedEvents()

            if (events.isEmpty()) {
                hasMoreEvents = false
            }

            republish(events)
        }
    }

    private fun getUnpublishedEvents(): List<ReviewEvent> {
        return questionReviewEventLogRepository.getUnPublishedEvent()
            .stream()
            .map { log -> SQSEvent.objectMapper.readValue(log.payload, ReviewEvent::class.java) }
            .toList()
    }

    private suspend fun republish(events: List<ReviewEvent>) {
        republishCoroutineScope.launch {
            events.map { event ->
                launch {
                    snsClient.publish(event.toRequest())
                    questionReviewEventLogRepository.publish(event.eventId)
                }
            }
        }.join()
    }
}