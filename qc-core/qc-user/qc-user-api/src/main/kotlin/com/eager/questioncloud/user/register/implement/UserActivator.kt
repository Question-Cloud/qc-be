package com.eager.questioncloud.user.register.implement

import com.eager.questioncloud.point.api.internal.PointCommandAPI
import com.eager.questioncloud.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserActivator(
    private val userRepository: UserRepository,
    private val pointCommandAPI: PointCommandAPI
) {
    fun activate(userId: Long) {
        val user = userRepository.getUser(userId)
        user.active()
        userRepository.save(user)
        
        pointCommandAPI.initialize(user.uid)
    }
}