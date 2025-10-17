package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.event.SubscribeEvent
import com.eager.questioncloud.common.event.SubscribeEventType
import com.eager.questioncloud.subscribe.implement.SubscribeProcessor
import com.eager.questioncloud.subscribe.implement.SubscribeValidator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscribeService(
    private val subscribeValidator: SubscribeValidator,
    private val subscribeProcessor: SubscribeProcessor,
    private val eventPublisher: EventPublisher
) {
    @Transactional
    fun subscribe(userId: Long, creatorId: Long) {
        subscribeValidator.validate(userId, creatorId)
        subscribeProcessor.subscribe(userId, creatorId)
        eventPublisher.publish(SubscribeEvent(creatorId, SubscribeEventType.SUBSCRIBE))
    }
    
    @Transactional
    fun unSubscribe(userId: Long, creatorId: Long) {
        subscribeProcessor.unSubscribe(userId, creatorId)
        eventPublisher.publish(SubscribeEvent(creatorId, SubscribeEventType.UNSUBSCRIBE))
    }
}
