package com.eager.questioncloud.application.api.authentication.service

import com.eager.questioncloud.application.api.authentication.dto.AuthenticationToken
import com.eager.questioncloud.application.api.authentication.dto.SocialAuthenticationResult
import com.eager.questioncloud.application.api.authentication.implement.AuthenticationProcessor
import com.eager.questioncloud.application.api.authentication.implement.AuthenticationTokenManager
import com.eager.questioncloud.core.domain.user.enums.AccountType
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationProcessor: AuthenticationProcessor,
    private val authenticationTokenManager: AuthenticationTokenManager,
) {
    fun login(email: String, password: String): AuthenticationToken {
        val user = authenticationProcessor.emailPasswordAuthentication(email, password)
        return authenticationTokenManager.create(user.uid!!)
    }

    fun socialLogin(accountType: AccountType, code: String): SocialAuthenticationResult {
        val socialAuthentication = authenticationProcessor.socialAuthentication(code, accountType)
        return socialAuthentication.toSocialAuthenticationResult(authenticationTokenManager)
    }

    fun refresh(refreshToken: String): AuthenticationToken {
        return authenticationTokenManager.refresh(refreshToken)
    }
}
