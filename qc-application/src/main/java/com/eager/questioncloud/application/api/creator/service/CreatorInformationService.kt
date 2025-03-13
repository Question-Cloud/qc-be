package com.eager.questioncloud.application.api.creator.service

import com.eager.questioncloud.application.api.creator.dto.CreatorInformation
import com.eager.questioncloud.application.api.creator.implement.CreatorInformationReader
import org.springframework.stereotype.Service

@Service
class CreatorInformationService(
    private val creatorInformationReader: CreatorInformationReader
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        return creatorInformationReader.getCreatorInformation(creatorId)
    }
}
