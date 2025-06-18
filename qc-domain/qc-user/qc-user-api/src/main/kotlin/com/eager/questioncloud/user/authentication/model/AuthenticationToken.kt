package com.eager.questioncloud.user.authentication.model

class AuthenticationToken(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun create(accessToken: String, refreshToken: String): AuthenticationToken {
            return AuthenticationToken(accessToken, refreshToken)
        }
    }
}
