package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class SubscribedCreatorInformationReader(
    private val subscribeRepository: SubscribeRepository,
    private val userQueryAPI: UserQueryAPI,
    private val creatorQueryAPI: CreatorQueryAPI,
) {
    fun getSubscribedCreatorInformation(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<SubscribedCreatorInformation> {
        val subscribedCreatorIds = subscribeRepository.getMySubscribedCreators(userId, pagingInformation)
        val creators = creatorQueryAPI.getCreators(subscribedCreatorIds)
        val creatorMap = creators.associateBy { it.creatorId }
        val creatorUserMap = userQueryAPI.getUsers(creators.map { it.userId }).associateBy { it.userId }
        val creatorSubscriberCountMap = subscribeRepository.countSubscriber(subscribedCreatorIds)

        return subscribedCreatorIds.map {
            val creator = creatorMap.getValue(it)
            val creatorUser = creatorUserMap.getValue(creator.userId)
            val creatorSubscriberCount = creatorSubscriberCountMap.getValue(it)

            SubscribedCreatorInformation(
                it,
                creatorUser.name,
                creatorUser.profileImage,
                creatorSubscriberCount,
                creator.mainSubject
            )
        }
    }
}