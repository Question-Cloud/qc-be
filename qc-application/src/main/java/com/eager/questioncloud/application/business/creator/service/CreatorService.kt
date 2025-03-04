package com.eager.questioncloud.application.business.creator.service

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import org.springframework.stereotype.Service

@Service
class CreatorService(
    private val creatorRepository: CreatorRepository
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        return creatorRepository.getCreatorInformation(creatorId)
    }
}
