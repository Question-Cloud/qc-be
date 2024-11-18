package com.eager.questioncloud.domain.social;

import com.eager.questioncloud.domain.social.SocialPlatform;
import com.eager.questioncloud.domain.user.vo.AccountType;

public abstract class SocialAPI {
    abstract String getAccessToken(String code);

    abstract SocialUserInfo getUserInfo(String accessToken);

    abstract SocialPlatform getSocialPlatform();

    record SocialAccessToken(String access_token) {
    }

    record SocialUserInfo(String uid, String email, String nickname, AccountType accountType) {
    }
}
