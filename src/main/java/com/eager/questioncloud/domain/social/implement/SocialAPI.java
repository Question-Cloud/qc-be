package com.eager.questioncloud.domain.social.implement;

import com.eager.questioncloud.domain.user.vo.AccountType;

public interface SocialAPI {
    String getAccessToken(String code);

    SocialUserInfo getUserInfo(String accessToken);

    record SocialAccessToken(String access_token) {
    }

    record SocialUserInfo(String uid, String email, String nickname, AccountType accountType) {
    }
}
