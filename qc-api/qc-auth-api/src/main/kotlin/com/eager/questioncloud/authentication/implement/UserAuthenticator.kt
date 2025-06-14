package com.eager.questioncloud.authentication.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.social.SocialPlatform
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserAuthenticator(
    private val socialAPIManager: SocialAPIManager,
    private val userRepository: UserRepository,
) {
    fun emailPasswordAuthentication(email: String, password: String): User {
        val user = userRepository.getUserByEmail(email)
        user.passwordAuthentication(password)
        user.checkUserStatus()
        return user
    }

    fun socialAuthentication(code: String, accountType: AccountType): User {
        val socialAccessToken = socialAPIManager.getAccessToken(code, SocialPlatform.valueOf(accountType.value))
        val socialUid = socialAPIManager.getSocialUid(socialAccessToken, SocialPlatform.valueOf(accountType.value))
        return userRepository.getSocialUser(accountType, socialUid)
            ?: throw CoreException(Error.NOT_REGISTERED_SOCIAL_USER)
    }
}
