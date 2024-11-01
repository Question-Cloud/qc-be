package com.eager.questioncloud.core.domain.social;

import com.eager.questioncloud.core.domain.user.vo.AccountType;
import lombok.Getter;

@Getter
public enum SocialPlatform {
    GOOGLE("GOOGLE"), KAKAO("KAKAO"), NAVER("NAVER");
    private final String value;

    SocialPlatform(String value) {
        this.value = value;
    }

    public static SocialPlatform from(AccountType accountType) {
        return SocialPlatform.valueOf(accountType.getValue());
    }
}
