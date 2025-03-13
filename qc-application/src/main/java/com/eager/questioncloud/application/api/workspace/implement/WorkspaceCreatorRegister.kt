package com.eager.questioncloud.application.api.workspace.implement

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.creator.model.Creator.Companion.create
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WorkspaceCreatorRegister(
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun register(user: User, mainSubject: Subject, introduction: String): Creator {
        setCreator(user)
        return creatorRepository.save(create(user.uid!!, mainSubject, introduction))
    }

    private fun setCreator(user: User) {
        user.setCreator()
        userRepository.save(user)
    }
}
