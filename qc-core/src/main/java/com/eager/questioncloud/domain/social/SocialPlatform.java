package com.eager.questioncloud.domain.social;

import com.eager.questioncloud.domain.user.AccountType;
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
