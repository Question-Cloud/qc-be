package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.event.annotation.IdempotentEvent
import com.eager.questioncloud.question.handler.UpdateReviewStatisticsHandler
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateReviewStatisticsListener(
    private val updateReviewStatisticsHandler: UpdateReviewStatisticsHandler
) {
    @SqsListener("update-question-review-statistics.fifo")
    @IdempotentEvent
    fun handle(@Payload event: ReviewEvent) {
        updateReviewStatisticsHandler.updateByRegisteredReview(event)
    }
}