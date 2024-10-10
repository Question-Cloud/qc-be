package com.eager.questioncloud.social.implement;

import com.eager.questioncloud.social.domain.SocialUserInfo;

public interface SocialAPI {
    String getAccessToken(String code);

    SocialUserInfo getUserInfo(String accessToken);
}
