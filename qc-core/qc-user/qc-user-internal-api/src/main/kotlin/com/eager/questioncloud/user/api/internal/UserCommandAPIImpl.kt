package com.eager.questioncloud.user.api.internal

import com.eager.questioncloud.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserCommandAPIImpl(
    private val userRepository: UserRepository
) : UserCommandAPI {
    override fun toCreator(userId: Long) {
        val user = userRepository.getUser(userId)
        user.setCreator()
        userRepository.save(user)
    }
}