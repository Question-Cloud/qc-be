package com.eager.questioncloud.creator.service

import com.eager.questioncloud.creator.dto.CreatorInformation
import com.eager.questioncloud.creator.implement.CreatorInformationReader
import org.springframework.stereotype.Service

@Service
class CreatorInformationService(
    private val creatorInformationReader: CreatorInformationReader
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        return creatorInformationReader.getCreatorInformation(creatorId)
    }
}
