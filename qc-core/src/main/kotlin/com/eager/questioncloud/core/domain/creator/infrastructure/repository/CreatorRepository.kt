package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.model.Creator

interface CreatorRepository {
    fun findById(creatorId: Long): Creator

    fun findByUserId(userId: Long): Creator?

    fun findByIdIn(creatorIds: List<Long>): List<Creator>

    fun existsById(creatorId: Long): Boolean

    fun save(creator: Creator): Creator

    fun deleteAllInBatch()
}
