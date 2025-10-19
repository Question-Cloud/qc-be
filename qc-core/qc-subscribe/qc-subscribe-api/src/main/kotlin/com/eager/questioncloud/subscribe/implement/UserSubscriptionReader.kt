package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.dto.UserSubscriptionDetail
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class UserSubscriptionReader(
    private val subscribeRepository: SubscribeRepository,
    private val userQueryAPI: UserQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
) {
    fun getUserSubscriptionDetails(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<UserSubscriptionDetail> {
        val subscribedCreatorIds = subscribeRepository.getMySubscribedCreators(userId, pagingInformation)
        val creators = creatorQueryAPI.getCreators(subscribedCreatorIds)
        val creatorMap = creators.associateBy { it.creatorId }
        val creatorUserMap = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }
        
        return subscribedCreatorIds.map {
            val creator = creatorMap.getValue(it)
            val creatorUser = creatorUserMap.getValue(creator.userId)
            val creatorSubscriberCount = creator.subscriberCount
            
            UserSubscriptionDetail(
                it,
                creatorUser.name,
                creatorUser.profileImage,
                creatorSubscriberCount,
                creator.mainSubject
            )
        }
    }
    
    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
    
    fun isSubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }
}