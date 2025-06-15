package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class SubscribedCreatorInformationReader(
    private val subscribeRepository: SubscribeRepository,
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
) {
    fun getSubscribedCreatorInformation(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<SubscribedCreatorInformation> {
        val subscribedCreatorIds = subscribeRepository.getMySubscribedCreators(userId, pagingInformation)
        val creators = creatorRepository.findByIdIn(subscribedCreatorIds)
        val creatorMap = creators.associateBy { it.id }
        val creatorUserMap = userRepository.findByUidIn(creators.map { it.userId }).associateBy { it.uid }
        val creatorSubscriberCountMap = subscribeRepository.countSubscriber(subscribedCreatorIds)

        return subscribedCreatorIds.map {
            val creator = creatorMap.getValue(it)
            val creatorUser = creatorUserMap.getValue(creator.userId)
            val creatorStatistics = creatorSubscriberCountMap.getValue(it)

            SubscribedCreatorInformation(
                it,
                creatorUser.userInformation.name,
                creatorUser.userInformation.profileImage,
                creatorStatistics,
                creator.mainSubject
            )
        }
    }
}