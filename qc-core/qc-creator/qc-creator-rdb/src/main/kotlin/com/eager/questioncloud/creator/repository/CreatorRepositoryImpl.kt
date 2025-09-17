package com.eager.questioncloud.creator.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.entity.CreatorEntity
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
        return creatorJpaRepository.save(CreatorEntity.from(creator)).toModel()
    }
    
    override fun deleteAllInBatch() {
        creatorJpaRepository.deleteAllInBatch()
    }
}
