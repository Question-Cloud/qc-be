package com.eager.questioncloud.application.api.authentication.implement

import com.eager.questioncloud.application.api.authentication.dto.SocialAuthentication
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.social.SocialAPIManager
import com.eager.questioncloud.social.SocialPlatform
import org.springframework.stereotype.Component

@Component
class AuthenticationProcessor(
    private val socialAPIManager: SocialAPIManager,
    private val userRepository: UserRepository,
) {
    fun emailPasswordAuthentication(email: String, password: String): User {
        val user = userRepository.getUserByEmail(email)
        user.passwordAuthentication(password)
        user.checkUserStatus()
        return user
    }

    fun socialAuthentication(code: String, accountType: AccountType): SocialAuthentication {
        val socialAccessToken = socialAPIManager.getAccessToken(code, SocialPlatform.valueOf(accountType.value))
        val socialUid = socialAPIManager.getSocialUid(socialAccessToken, SocialPlatform.valueOf(accountType.value))
        return userRepository.getSocialUser(accountType, socialUid)
            ?.let {
                it.checkUserStatus()
                SocialAuthentication.create(it, socialAccessToken)
            }
            ?: SocialAuthentication.create(null, socialAccessToken)
    }
}
