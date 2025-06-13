package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.dto.CreatorProfile
import com.eager.questioncloud.core.domain.creator.model.Creator

interface CreatorRepository {
    fun findById(creatorId: Long): Creator

    fun findByIdIn(creatorIds: List<Long>): List<Creator>

    fun existsById(creatorId: Long): Boolean

    fun getCreatorProfile(creatorId: Long): CreatorProfile

    fun getCreatorProfile(creatorIds: List<Long>): Map<Long, CreatorProfile>

    fun save(creator: Creator): Creator

    fun deleteAllInBatch()
}
