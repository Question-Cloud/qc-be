package com.eager.questioncloud.creator.implement

import com.eager.questioncloud.creator.domain.CreatorProfile
import com.eager.questioncloud.creator.dto.CreatorInformation
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.creator.repository.CreatorStatisticsRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import org.springframework.stereotype.Component

@Component
class CreatorInformationReader(
    private val creatorRepository: CreatorRepository,
    private val creatorStatisticsRepository: CreatorStatisticsRepository,
    private val userQueryAPI: UserQueryAPI,
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        val creator = creatorRepository.findById(creatorId)
        val creatorUser = userQueryAPI.getUser(creator.userId)
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId)
        val creatorProfile = CreatorProfile(
            creator.id,
            creatorUser.name,
            creatorUser.profileImage,
            creator.mainSubject,
            creatorUser.email,
            creator.introduction
        )
        
        return CreatorInformation(
            creatorProfile,
            creatorStatistics.salesCount,
            creatorStatistics.averageRateOfReview,
            creatorStatistics.subscriberCount
        )
    }
}