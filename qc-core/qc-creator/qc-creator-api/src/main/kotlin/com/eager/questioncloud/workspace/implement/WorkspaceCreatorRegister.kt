package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.user.api.internal.UserCommandAPI
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WorkspaceCreatorRegister(
    private val creatorRepository: CreatorRepository,
    private val userCommandAPI: UserCommandAPI,
) {
    @Transactional
    fun register(userId: Long, mainSubject: String, introduction: String): Creator {
        userCommandAPI.toCreator(userId)
        return creatorRepository.save(Creator.create(userId, mainSubject, introduction))
    }
}
