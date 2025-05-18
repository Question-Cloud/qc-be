package com.eager.questioncloud.application.api.authentication.dto

class SocialAuthenticateResponse(
    val isRegistered: Boolean,
    val registerToken: String?,
    val authenticationToken: AuthenticationToken?,
) {
    companion object {
        fun create(socialAuthenticationResult: SocialAuthenticationResult): SocialAuthenticateResponse {
            return SocialAuthenticateResponse(
                socialAuthenticationResult.isRegistered,
                socialAuthenticationResult.registerToken,
                socialAuthenticationResult.authenticationToken
            )
        }
    }
}

