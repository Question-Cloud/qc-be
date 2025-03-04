package com.eager.questioncloud.application.business.user.service

import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userRepository: UserRepository
) {
    fun updateUserInformation(user: User, name: String, profileImage: String) {
        user.updateUserInformation(name, profileImage)
        userRepository.save(user)
    }
}
