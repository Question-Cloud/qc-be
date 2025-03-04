package com.eager.questioncloud.social

interface SocialAPI {
    fun getAccessToken(code: String): String

    fun getUserInfo(accessToken: String): SocialUserInfo

    fun getSocialPlatform(): SocialPlatform
}
