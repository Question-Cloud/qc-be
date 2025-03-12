package com.eager.questioncloud.application.business.creator.service

import com.eager.questioncloud.application.business.creator.dto.CreatorInformation
import com.eager.questioncloud.application.business.creator.implement.CreatorReader
import org.springframework.stereotype.Service

@Service
class CreatorService(
    private val creatorReader: CreatorReader
) {
    fun getCreatorInformation(creatorId: Long): CreatorInformation {
        return creatorReader.getCreatorInformation(creatorId)
    }
}
