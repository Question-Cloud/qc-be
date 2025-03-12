package com.eager.questioncloud.application.business.subscribe.service

import com.eager.questioncloud.application.business.creator.dto.CreatorInformation
import com.eager.questioncloud.application.business.creator.implement.CreatorReader
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import org.springframework.stereotype.Service

@Service
class FeedSubscribeService(
    private val subscribeRepository: SubscribeRepository,
    private val creatorReader: CreatorReader,
) {
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<CreatorInformation> {
        val subscribedCreatorIds = subscribeRepository.getMySubscribedCreators(userId, pagingInformation)
        return creatorReader.getCreatorInformation(subscribedCreatorIds)
    }

    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
}
