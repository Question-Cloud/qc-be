package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation
import com.eager.questioncloud.core.domain.creator.model.Creator

interface CreatorRepository {
    fun existsById(creatorId: Long): Boolean

    fun getCreatorInformation(creatorId: Long): CreatorInformation

    fun save(creator: Creator): Creator

    fun deleteAllInBatch()
}
