package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import org.springframework.stereotype.Component

@Component
class SubscribeValidator(
    private val creatorQueryAPI: CreatorQueryAPI,
    private val subscribeRepository: SubscribeRepository,
) {
    fun validate(userId: Long, creatorId: Long) {
        if (!isActiveCreator(creatorId)) {
            throw CoreException(Error.NOT_FOUND)
        }
        
        if (isAlreadySubscribed(userId, creatorId)) {
            throw CoreException(Error.ALREADY_SUBSCRIBE_CREATOR)
        }
    }
    
    private fun isActiveCreator(creatorId: Long): Boolean {
        return creatorQueryAPI.isExistsById(creatorId)
    }
    
    private fun isAlreadySubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }
}