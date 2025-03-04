package com.eager.questioncloud.social;

import lombok.Getter;

@Getter
public enum SocialPlatform {
    GOOGLE("GOOGLE"), KAKAO("KAKAO"), NAVER("NAVER");
    private final String value;

    SocialPlatform(String value) {
        this.value = value;
    }
}
