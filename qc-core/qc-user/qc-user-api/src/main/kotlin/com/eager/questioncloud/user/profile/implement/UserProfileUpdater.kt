package com.eager.questioncloud.user.profile.implement

import com.eager.questioncloud.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserProfileUpdater(
    private val userRepository: UserRepository,
) {
    fun updateUserInformation(userId: Long, name: String, profileImage: String) {
        val user = userRepository.getUser(userId)
        user.updateUserInformation(name, profileImage)
        userRepository.save(user)
    }
}