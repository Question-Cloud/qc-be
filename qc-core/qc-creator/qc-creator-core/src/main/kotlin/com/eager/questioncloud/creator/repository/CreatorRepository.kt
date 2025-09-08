package com.eager.questioncloud.creator.repository

import com.eager.questioncloud.creator.domain.Creator

interface CreatorRepository {
    fun findById(creatorId: Long): Creator
    
    fun findByUserId(userId: Long): Creator?
    
    fun findByIdIn(creatorIds: List<Long>): List<Creator>
    
    fun existsById(creatorId: Long): Boolean
    
    fun save(creator: Creator): Creator
    
    fun deleteAllInBatch()
}
