package com.eager.questioncloud.application.business.subscribe.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.subscribe.dto.SubscribeDetail
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import org.springframework.stereotype.Service

@Service
class FeedSubscribeService(
    private val subscribeRepository: SubscribeRepository
) {
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<SubscribeDetail> {
        return subscribeRepository.getMySubscribes(userId, pagingInformation)
    }

    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
}
