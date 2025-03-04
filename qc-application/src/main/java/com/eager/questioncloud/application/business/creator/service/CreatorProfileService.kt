package com.eager.questioncloud.application.business.creator.service

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile
import org.springframework.stereotype.Service

@Service
class CreatorProfileService(
    private val creatorRepository: CreatorRepository
) {
    fun updateCreatorProfile(creator: Creator, creatorProfile: CreatorProfile) {
        creator.updateProfile(creatorProfile)
        creatorRepository.save(creator)
    }
}
