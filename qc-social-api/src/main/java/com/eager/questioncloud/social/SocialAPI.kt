package com.eager.questioncloud.social;

public abstract class SocialAPI {
    abstract String getAccessToken(String code);

    abstract SocialUserInfo getUserInfo(String accessToken);

    abstract SocialPlatform getSocialPlatform();

    record SocialAccessToken(String access_token) {
    }

    record SocialUserInfo(String uid, String email, String nickname, SocialPlatform socialPlatform) {
    }
}
