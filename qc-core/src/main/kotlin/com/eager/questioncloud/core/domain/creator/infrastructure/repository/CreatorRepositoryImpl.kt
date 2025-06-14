package com.eager.questioncloud.core.domain.creator.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.CreatorEntity.Companion.from
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.springframework.stereotype.Repository

@Repository
class CreatorRepositoryImpl(
    private val creatorJpaRepository: CreatorJpaRepository,
) : CreatorRepository {

    override fun findById(creatorId: Long): Creator {
        return creatorJpaRepository.findById(creatorId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun findByUserId(userId: Long): Creator? {
        return creatorJpaRepository.findByUserId(userId)?.toModel()
    }

    override fun findByIdIn(creatorIds: List<Long>): List<Creator> {
        return creatorJpaRepository.findByIdIn(creatorIds).map { it.toModel() }
    }

    override fun existsById(creatorId: Long): Boolean {
        return creatorJpaRepository.existsById(creatorId)
    }

    override fun save(creator: Creator): Creator {
        return creatorJpaRepository.save(from(creator)).toModel()
    }

    override fun deleteAllInBatch() {
        creatorJpaRepository.deleteAllInBatch()
    }
}
