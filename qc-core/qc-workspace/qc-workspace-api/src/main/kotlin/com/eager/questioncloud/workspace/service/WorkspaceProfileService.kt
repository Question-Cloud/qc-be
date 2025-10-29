package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.creator.api.internal.CreatorCommandAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.workspace.dto.CreatorProfile
import org.springframework.stereotype.Service

@Service
class WorkspaceProfileService(
    private val creatorQueryAPI: CreatorQueryAPI,
    private val creatorCommandAPI: CreatorCommandAPI,
) {
    fun updateCreatorProfile(creatorId: Long, mainSubject: String, introduction: String) {
        creatorCommandAPI.updateCreatorProfile(creatorId, mainSubject, introduction)
    }
    
    fun getProfile(creatorId: Long): CreatorProfile {
        val creatorQueryData = creatorQueryAPI.getCreator(creatorId)
        return CreatorProfile(creatorQueryData.mainSubject, creatorQueryData.introduction)
    }
}
