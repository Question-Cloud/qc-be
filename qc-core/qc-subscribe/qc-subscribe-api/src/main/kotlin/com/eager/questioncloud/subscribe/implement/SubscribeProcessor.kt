package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.infrastructure.repository.SubscribeRepository
import org.springframework.stereotype.Component

@Component
class SubscribeProcessor(
    private val subscribeRepository: SubscribeRepository,
    private val creatorQueryAPI: CreatorQueryAPI,
) {
    fun subscribe(userId: Long, creatorId: Long) {
        if (!isActiveCreator(creatorId)) {
            throw CoreException(Error.NOT_FOUND)
        }

        if (isAlreadySubscribed(userId, creatorId)) {
            throw CoreException(Error.ALREADY_SUBSCRIBE_CREATOR)
        }

        subscribeRepository.save(Subscribe.create(userId, creatorId))
    }

    fun unSubscribe(userId: Long, creatorId: Long) {
        subscribeRepository.unSubscribe(userId, creatorId)
    }

    private fun isActiveCreator(creatorId: Long): Boolean {
        return creatorQueryAPI.isExistsById(creatorId)
    }

    private fun isAlreadySubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }
}
