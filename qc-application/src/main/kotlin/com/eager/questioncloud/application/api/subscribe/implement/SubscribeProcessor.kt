package com.eager.questioncloud.application.api.subscribe.implement

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe.Companion.create
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Component

@Component
class SubscribeProcessor(
    private val subscribeRepository: SubscribeRepository,
    private val creatorRepository: CreatorRepository,
) {
    fun subscribe(userId: Long, creatorId: Long) {
        if (!isActiveCreator(creatorId)) {
            throw CoreException(Error.NOT_FOUND)
        }

        if (isAlreadySubscribed(userId, creatorId)) {
            throw CoreException(Error.ALREADY_SUBSCRIBE_CREATOR)
        }

        subscribeRepository.save(create(userId, creatorId))
    }

    fun unSubscribe(userId: Long, creatorId: Long) {
        subscribeRepository.unSubscribe(userId, creatorId)
    }

    private fun isActiveCreator(creatorId: Long): Boolean {
        return creatorRepository.existsById(creatorId)
    }

    private fun isAlreadySubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }
}
