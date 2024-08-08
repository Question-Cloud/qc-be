package com.eager.questioncloud.social;

public interface SocialProcessor {
    String getAccessToken(String code);

    SocialUserInfo getUserInfo(String accessToken);
}
