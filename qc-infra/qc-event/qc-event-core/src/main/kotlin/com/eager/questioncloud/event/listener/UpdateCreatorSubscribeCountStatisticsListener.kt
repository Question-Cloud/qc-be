package com.eager.questioncloud.event.listener

import com.eager.questioncloud.common.event.SubscribeEvent
import com.eager.questioncloud.creator.handler.UpdateCreatorSubscribeCountStatisticsHandler
import com.eager.questioncloud.event.annotation.IdempotentEvent
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.context.annotation.Profile
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
class UpdateCreatorSubscribeCountStatisticsListener(
    private val updateCreatorSubscribeCountStatisticsHandler: UpdateCreatorSubscribeCountStatisticsHandler
) {
    @SqsListener("update-creator-subscribe-statistics.fifo")
    @IdempotentEvent
    fun handle(@Payload event: SubscribeEvent) {
        updateCreatorSubscribeCountStatisticsHandler.handler(event)
    }
}