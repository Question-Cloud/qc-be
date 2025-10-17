package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import org.springframework.stereotype.Component

@Component
class SubscribeProcessor(
    private val subscribeRepository: SubscribeRepository,
) {
    fun subscribe(userId: Long, creatorId: Long) {
        subscribeRepository.save(Subscribe.create(userId, creatorId))
    }
    
    fun unSubscribe(userId: Long, creatorId: Long) {
        subscribeRepository.unSubscribe(userId, creatorId)
    }
}
