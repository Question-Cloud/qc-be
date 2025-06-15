package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.enums.Subject
import org.springframework.stereotype.Service

@Service
class WorkspaceProfileService(
    private val creatorRepository: CreatorRepository,
) {
    fun updateCreatorProfile(userId: Long, mainSubject: Subject, introduction: String) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        creator.updateProfile(mainSubject.value, introduction)
        creatorRepository.save(creator)
    }

    fun me(userId: Long): Creator {
        return creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
    }
}
