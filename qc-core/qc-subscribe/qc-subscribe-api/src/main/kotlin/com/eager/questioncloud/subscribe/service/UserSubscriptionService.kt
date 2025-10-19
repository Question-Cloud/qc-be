package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.UserSubscriptionDetail
import com.eager.questioncloud.subscribe.implement.UserSubscriptionReader
import org.springframework.stereotype.Service

@Service
class UserSubscriptionService(
    private val userSubscriptionReader: UserSubscriptionReader,
) {
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<UserSubscriptionDetail> {
        return userSubscriptionReader.getUserSubscriptionDetails(userId, pagingInformation)
    }
    
    fun countMySubscribe(userId: Long): Int {
        return userSubscriptionReader.countMySubscribe(userId)
    }
    
    fun isSubscribed(userId: Long, creatorId: Long): Boolean {
        return userSubscriptionReader.isSubscribed(userId, creatorId)
    }
}