package com.eager.questioncloud.user.account.implement

import com.eager.questioncloud.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserAccountUpdater(
    private val userRepository: UserRepository
) {
    fun changePassword(userId: Long, newPassword: String) {
        val user = userRepository.getUser(userId)
        user.changePassword(newPassword)
        userRepository.save(user)
    }
}
