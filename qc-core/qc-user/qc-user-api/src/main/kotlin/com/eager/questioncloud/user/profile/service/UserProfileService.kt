package com.eager.questioncloud.user.profile.service

import com.eager.questioncloud.user.dto.MyInformation
import com.eager.questioncloud.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userRepository: UserRepository
) {
    fun updateUserInformation(userId: Long, name: String, profileImage: String) {
        val user = userRepository.getUser(userId)
        user.updateUserInformation(name, profileImage)
        userRepository.save(user)
    }
    
    fun getMyInformation(userId: Long): MyInformation {
        val user = userRepository.getUser(userId)
        return MyInformation.from(user)
    }
}
