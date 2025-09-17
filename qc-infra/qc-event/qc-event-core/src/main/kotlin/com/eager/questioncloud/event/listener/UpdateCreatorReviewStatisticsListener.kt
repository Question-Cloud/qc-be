package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.creator.listener.UpdateCreatorReviewStatisticsHandler
import com.eager.questioncloud.event.annotation.IdempotentEvent
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UpdateCreatorReviewStatisticsListener(
    private val updateCreatorReviewStatisticsHandler: UpdateCreatorReviewStatisticsHandler
) {
    @SqsListener("update-creator-review-statistics.fifo")
    @IdempotentEvent
    fun handle(@Payload event: ReviewEvent) {
        updateCreatorReviewStatisticsHandler.updateCreatorReviewStatistics(event)
    }
}