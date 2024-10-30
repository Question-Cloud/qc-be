package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.user.vo.AccountType;

public interface SocialAPI {
    String getAccessToken(String code);

    SocialUserInfo getUserInfo(String accessToken);

    record SocialAccessToken(String access_token) {
    }

    record SocialUserInfo(String uid, String email, String nickname, AccountType accountType) {
    }
}
