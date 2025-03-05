package com.eager.questioncloud.application.api.authentication.dto

import com.eager.questioncloud.core.domain.user.model.User

class SocialAuthentication(
    val user: User?,
    val socialAccessToken: String
) {
    val isRegistered: Boolean
        get() = this.user != null

    companion object {
        fun create(user: User?, socialAccessToken: String): SocialAuthentication {
            return SocialAuthentication(user, socialAccessToken)
        }
    }
}
