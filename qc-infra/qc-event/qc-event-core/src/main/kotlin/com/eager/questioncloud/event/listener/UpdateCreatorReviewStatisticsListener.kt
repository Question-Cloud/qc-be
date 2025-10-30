package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.creator.handler.UpdateCreatorReviewStatisticsHandler
import com.eager.questioncloud.event.annotation.IdempotentEvent
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.context.annotation.Profile
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Profile("prod", "local")
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