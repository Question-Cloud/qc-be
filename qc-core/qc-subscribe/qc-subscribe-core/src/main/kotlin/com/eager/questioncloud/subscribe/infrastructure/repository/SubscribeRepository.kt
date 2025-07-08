package com.eager.questioncloud.subscribe.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.domain.Subscribe

interface SubscribeRepository {
    fun save(subscribe: Subscribe): Subscribe

    fun isSubscribed(subscriberId: Long, creatorId: Long): Boolean

    fun unSubscribe(subscriberId: Long, creatorId: Long)

    fun countSubscriber(creatorId: Long): Int

    fun countSubscriber(creatorIds: List<Long>): Map<Long, Int>

    fun getMySubscribedCreators(userId: Long, pagingInformation: PagingInformation): List<Long>

    fun countMySubscribe(userId: Long): Int

    fun deleteAllInBatch()
}
