package com.eager.questioncloud.social

import org.springframework.stereotype.Component

@Component
class SocialAPIManager(
    private val socialAPIs: List<SocialAPI>
) {
    fun getAccessToken(code: String, socialPlatform: SocialPlatform): String {
        for (socialAPI in socialAPIs) {
            if (socialAPI.getSocialPlatform() == socialPlatform) {
                return socialAPI.getAccessToken(code)
            }
        }
        throw FailSocialLoginException()
    }

    fun getSocialUid(accessToken: String, socialPlatform: SocialPlatform): String {
        for (socialAPI in socialAPIs) {
            if (socialAPI.getSocialPlatform() == socialPlatform) {
                return socialAPI.getUserInfo(accessToken).uid
            }
        }
        throw FailSocialLoginException()
    }
}
