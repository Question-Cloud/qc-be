package com.eager.questioncloud.creator.listener

import com.eager.questioncloud.common.event.*
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("prod", "local")
@Component
@QueueListener(queueName = "update-creator-subscribe-statistics", type = SubscribeEvent::class)
class UpdateCreatorSubscribeCountStatisticsListener(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
) : MessageListener<SubscribeEvent> {
    
    @IdempotentEvent
    override fun onMessage(event: SubscribeEvent) {
        when (event.type) {
            SubscribeEventType.SUBSCRIBE -> creatorStatisticsRepository.incrementSubscribeCount(event.creatorId)
            SubscribeEventType.UNSUBSCRIBE -> creatorStatisticsRepository.decrementSubscribeCount(event.creatorId)
        }
    }
}