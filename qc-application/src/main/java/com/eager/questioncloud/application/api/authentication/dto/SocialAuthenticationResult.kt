package com.eager.questioncloud.application.api.authentication.dto

class SocialAuthenticationResult(
    val isRegistered: Boolean,
    val registerToken: String?,
    val authenticationToken: AuthenticationToken?,
) {
    companion object {
        fun success(authenticationToken: AuthenticationToken?): SocialAuthenticationResult {
            return SocialAuthenticationResult(true, null, authenticationToken)
        }

        fun notRegister(registerToken: String?): SocialAuthenticationResult {
            return SocialAuthenticationResult(false, registerToken, null)
        }
    }
}
