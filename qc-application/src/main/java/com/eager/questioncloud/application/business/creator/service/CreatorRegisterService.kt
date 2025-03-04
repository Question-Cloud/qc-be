package com.eager.questioncloud.application.business.creator.service

import com.eager.questioncloud.application.business.creator.implement.CreatorRegister
import com.eager.questioncloud.core.domain.creator.implement.CreatorStatisticsProcessor
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.stereotype.Service

@Service
class CreatorRegisterService(
    private val creatorRegister: CreatorRegister,
    private val creatorStatisticsProcessor: CreatorStatisticsProcessor,
) {
    fun register(user: User, creatorProfile: CreatorProfile): Creator {
        val creator = creatorRegister.register(user, creatorProfile)
        creatorStatisticsProcessor.initializeCreatorStatistics(creator.id!!)
        return creator
    }
}
