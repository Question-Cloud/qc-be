package com.eager.questioncloud.application.business.subscribe.service

import com.eager.questioncloud.application.business.subscribe.implement.SubscribeProcessor
import com.eager.questioncloud.core.domain.subscribe.event.SubscribedEvent
import com.eager.questioncloud.core.domain.subscribe.event.UnsubscribedEvent
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SubscribeService(
    private val subscribeProcessor: SubscribeProcessor,
    private val subscribeRepository: SubscribeRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun subscribe(userId: Long, creatorId: Long) {
        subscribeProcessor.subscribe(userId, creatorId)
        applicationEventPublisher.publishEvent(SubscribedEvent.create(creatorId))
    }

    fun unSubscribe(userId: Long, creatorId: Long) {
        subscribeProcessor.unSubscribe(userId, creatorId)
        applicationEventPublisher.publishEvent(UnsubscribedEvent.create(creatorId))
    }

    fun isSubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }

    fun countSubscriber(creatorId: Long): Int {
        return subscribeRepository.countSubscriber(creatorId)
    }
}
