package com.eager.questioncloud.application.business.review.implement

import com.eager.questioncloud.application.business.review.event.ReviewEvent
import com.eager.questioncloud.application.business.review.event.ReviewEventType
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewEventLogRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog
import io.hypersistence.tsid.TSID
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import software.amazon.awssdk.services.sns.SnsClient
import java.time.LocalDateTime

@Component
class QuestionReviewEventProcessor(
    private val questionReviewEventLogRepository: QuestionReviewEventLogRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val snsClient: SnsClient,
) {
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
}