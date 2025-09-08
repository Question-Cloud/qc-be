package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import org.springframework.stereotype.Service

@Service
class WorkspaceProfileService(
    private val creatorRepository: CreatorRepository,
) {
    fun updateCreatorProfile(userId: Long, mainSubject: String, introduction: String) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        creator.updateProfile(mainSubject, introduction)
        creatorRepository.save(creator)
    }
    
    fun me(userId: Long): Creator {
        return creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
    }
}
