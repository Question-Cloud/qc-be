package com.eager.questioncloud.social;

public interface SocialAPI {
    String getAccessToken(String code);

    SocialUserInfo getUserInfo(String accessToken);
}
