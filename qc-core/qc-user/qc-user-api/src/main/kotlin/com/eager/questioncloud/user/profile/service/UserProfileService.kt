package com.eager.questioncloud.user.profile.service

import com.eager.questioncloud.user.common.implement.UserFinder
import com.eager.questioncloud.user.dto.MyInformation
import com.eager.questioncloud.user.profile.implement.UserProfileUpdater
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userFinder: UserFinder,
    private val userProfileUpdater: UserProfileUpdater
) {
    fun updateUserInformation(userId: Long, name: String, profileImage: String) {
        userProfileUpdater.updateUserInformation(userId, name, profileImage)
    }
    
    fun getMyInformation(userId: Long): MyInformation {
        val user = userFinder.getById(userId)
        return MyInformation.from(user)
    }
}
