package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.creator.dto.CreatorInformation
import com.eager.questioncloud.creator.dto.CreatorProfile
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class CreatorInformationReader(
    private val creatorRepository: CreatorRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val subscribeRepository: SubscribeRepository,
    private val userRepository: UserRepository,
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        val creator = creatorRepository.findById(creatorId)
        val creatorUser = userRepository.getUser(creator.userId)
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
        val subscriberCount = subscribeRepository.countSubscriber(creatorId)
        val creatorProfile = CreatorProfile(
            creator.id,
            creatorUser.userInformation.name,
            creatorUser.userInformation.profileImage,
            creator.mainSubject,
            creatorUser.userInformation.email,
            creator.introduction
        )

        return CreatorInformation(
            creatorProfile,
            creatorStatistics.salesCount,
            creatorStatistics.averageRateOfReview,
            subscriberCount
        )
    }

    fun getCreatorInformation(creatorIds: List<Long>): List<CreatorInformation> {
        val creators = creatorRepository.findByIdIn(creatorIds)
        val creatorUserMap = userRepository.findByUidIn(creators.map { it.userId }).associateBy { it.uid }
        val creatorStatisticsMap = creatorStatisticsRepository.findByCreatorIdIn(creatorIds)
        val subscriberCountMap = subscribeRepository.countSubscriber(creatorIds)

        return creators.map {
            val creatorUser = creatorUserMap.getValue(it.userId)
            val creatorProfile = CreatorProfile(
                it.id,
                creatorUser.userInformation.name,
                creatorUser.userInformation.profileImage,
                it.mainSubject,
                creatorUser.userInformation.email,
                it.introduction
            )
            CreatorInformation(
                creatorProfile,
                creatorStatisticsMap.getValue(it.id).salesCount,
                creatorStatisticsMap.getValue(it.id).averageRateOfReview,
                subscriberCountMap.getValue(it.id)
            )
        }
    }
}