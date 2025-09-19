package com.eager.questioncloud.creator.handler

import com.eager.questioncloud.common.event.SubscribeEvent
import com.eager.questioncloud.common.event.SubscribeEventType
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateCreatorSubscribeCountStatisticsHandler(
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
) {
    @Transactional
    fun handler(subscribeEvent: SubscribeEvent) {
        when (subscribeEvent.type) {
            SubscribeEventType.SUBSCRIBE -> creatorStatisticsRepository.incrementSubscribeCount(subscribeEvent.creatorId)
            SubscribeEventType.UNSUBSCRIBE -> creatorStatisticsRepository.decrementSubscribeCount(subscribeEvent.creatorId)
        }
    }
}