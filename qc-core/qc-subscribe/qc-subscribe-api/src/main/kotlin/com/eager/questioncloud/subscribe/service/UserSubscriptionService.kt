package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.implement.SubscribedCreatorInformationReader
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import org.springframework.stereotype.Service

@Service
class UserSubscriptionService(
    private val subscribedCreatorInformationReader: SubscribedCreatorInformationReader,
    private val subscribeRepository: SubscribeRepository,
) {
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<SubscribedCreatorInformation> {
        return subscribedCreatorInformationReader.getSubscribedCreatorInformation(userId, pagingInformation)
    }
    
    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
    
    fun isSubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }
}