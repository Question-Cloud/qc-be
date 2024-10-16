package com.eager.questioncloud.social.implement;

import com.eager.questioncloud.user.vo.AccountType;

public interface SocialAPI {
    String getAccessToken(String code);

    SocialUserInfo getUserInfo(String accessToken);

    record SocialAccessToken(String access_token) {
    }

    record SocialUserInfo(String uid, String email, String nickname, AccountType accountType) {
    }
}
