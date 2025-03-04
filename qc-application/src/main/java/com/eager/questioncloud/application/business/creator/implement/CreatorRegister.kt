package com.eager.questioncloud.application.business.creator.implement

import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.creator.model.Creator.Companion.create
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CreatorRegister(
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun register(user: User, creatorProfile: CreatorProfile): Creator {
        setCreator(user)
        return creatorRepository.save(create(user.uid!!, creatorProfile))
    }

    private fun setCreator(user: User) {
        user.setCreator()
        userRepository.save(user)
    }
}
