package com.eager.questioncloud.user.api.internal

import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserQueryAPIImpl(
    private val userRepository: UserRepository
) : UserQueryAPI {
    override fun getUser(userId: Long): UserQueryData {
        val user = userRepository.getUser(userId)
        return UserQueryData(
            user.uid,
            user.userInformation.name,
            user.userInformation.profileImage,
            user.userInformation.email,
        )
    }

    override fun getUsers(userIds: List<Long>): List<UserQueryData> {
        val users = userRepository.findByUidIn(userIds)
        return users.map {
            UserQueryData(
                it.uid,
                it.userInformation.name,
                it.userInformation.profileImage,
                it.userInformation.email
            )
        }
    }
}