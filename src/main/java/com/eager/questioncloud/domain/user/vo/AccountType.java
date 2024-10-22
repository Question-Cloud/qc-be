package com.eager.questioncloud.domain.user.vo;

import lombok.Getter;

@Getter
public enum AccountType {
    EMAIL("EMAIL"), KAKAO("KAKAO"), GOOGLE("GOOGLE"), NAVER("NAVER"), GUEST("GUEST");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }
}
