package com.eager.questioncloud.application.api.authentication.model

import com.eager.questioncloud.application.api.authentication.dto.SocialAuthenticationResult
import com.eager.questioncloud.application.api.authentication.implement.AuthenticationTokenManager
import com.eager.questioncloud.core.domain.user.model.User

class SocialAuthentication(
    val user: User?,
    val socialAccessToken: String
) {
    fun toSocialAuthenticationResult(authenticationTokenManager: AuthenticationTokenManager): SocialAuthenticationResult {
        if (isRegistered()) {
            return SocialAuthenticationResult.success(authenticationTokenManager.create(user!!.uid!!))
        }
        return SocialAuthenticationResult.notRegister(socialAccessToken)
    }

    private fun isRegistered(): Boolean {
        return user != null
    }

    companion object {
        fun create(user: User?, socialAccessToken: String): SocialAuthentication {
            return SocialAuthentication(user, socialAccessToken)
        }
    }
}
