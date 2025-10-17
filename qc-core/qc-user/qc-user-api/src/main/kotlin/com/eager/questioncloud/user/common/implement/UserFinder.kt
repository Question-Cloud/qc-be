package com.eager.questioncloud.user.common.implement

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {
    fun getById(id: Long): User {
        return userRepository.getUser(id)
    }
    
    fun getByEmail(email: String): User {
        return userRepository.getUserByEmail(email)
    }
    
    fun getByPhone(phone: String): User {
        return userRepository.getUserByPhone(phone)
    }
}