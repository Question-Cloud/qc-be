package com.eager.questioncloud.application.business.subscribe.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import org.springframework.stereotype.Service

@Service
class FeedSubscribeService(
    private val subscribeRepository: SubscribeRepository,
    private val creatorRepository: CreatorRepository,
) {
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<CreatorInformation> {
        val subscribedCreatorIds = subscribeRepository.getMySubscribedCreators(userId, pagingInformation)
        return creatorRepository.getCreatorInformation(subscribedCreatorIds)
    }

    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
}
