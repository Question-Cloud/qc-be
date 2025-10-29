package com.eager.questioncloud.creator.api.internal

interface CreatorQueryAPI {
    fun getCreatorByUserId(userId: Long): CreatorQueryData
    
    fun getCreator(creatorId: Long): CreatorQueryData
    
    fun getCreators(creatorIds: List<Long>): List<CreatorQueryData>
    
    fun isExistsById(creatorId: Long): Boolean
}