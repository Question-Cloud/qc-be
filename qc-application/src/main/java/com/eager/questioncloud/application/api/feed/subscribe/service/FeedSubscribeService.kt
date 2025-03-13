package com.eager.questioncloud.application.api.feed.subscribe.service

import com.eager.questioncloud.application.api.creator.dto.CreatorInformation
import com.eager.questioncloud.application.api.creator.implement.CreatorInformationReader
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import org.springframework.stereotype.Service

@Service
class FeedSubscribeService(
    private val subscribeRepository: SubscribeRepository,
    private val creatorInformationReader: CreatorInformationReader,
) {
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<CreatorInformation> {
        val subscribedCreatorIds = subscribeRepository.getMySubscribedCreators(userId, pagingInformation)
        return creatorInformationReader.getCreatorInformation(subscribedCreatorIds)
    }

    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
}
