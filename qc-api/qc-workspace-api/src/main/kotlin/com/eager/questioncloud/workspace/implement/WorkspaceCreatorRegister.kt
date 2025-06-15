package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WorkspaceCreatorRegister(
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun register(userId: Long, mainSubject: Subject, introduction: String): Creator {
        val user = userRepository.getUser(userId)
        setCreator(user)
        return creatorRepository.save(Creator.create(user.uid, mainSubject.value, introduction))
    }

    private fun setCreator(user: User) {
        user.setCreator()
        userRepository.save(user)
    }
}
