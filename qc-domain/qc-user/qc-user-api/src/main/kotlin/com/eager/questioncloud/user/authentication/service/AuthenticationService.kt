package com.eager.questioncloud.user.authentication.service

import com.eager.questioncloud.user.authentication.implement.AuthenticationTokenGenerator
import com.eager.questioncloud.user.authentication.implement.AuthenticationTokenRefresher
import com.eager.questioncloud.user.authentication.implement.UserAuthenticator
import com.eager.questioncloud.user.authentication.model.AuthenticationToken
import com.eager.questioncloud.user.enums.AccountType
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userAuthenticator: UserAuthenticator,
    private val authenticationTokenGenerator: AuthenticationTokenGenerator,
    private val authenticationTokenRefresher: AuthenticationTokenRefresher
) {
    fun login(email: String, password: String): AuthenticationToken {
        val user = userAuthenticator.emailPasswordAuthentication(email, password)
        return authenticationTokenGenerator.createAuthenticationToken(user.uid)
    }

    fun socialLogin(accountType: AccountType, code: String): AuthenticationToken {
        val user = userAuthenticator.socialAuthentication(code, accountType)
        return authenticationTokenGenerator.createAuthenticationToken(user.uid)
    }

    fun refresh(refreshToken: String): AuthenticationToken {
        return authenticationTokenRefresher.refresh(refreshToken)
    }
}
