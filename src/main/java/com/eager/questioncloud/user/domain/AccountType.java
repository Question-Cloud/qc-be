package com.eager.questioncloud.user.domain;

import lombok.Getter;

@Getter
public enum AccountType {
    EMAIL("EMAIL"), KAKAO("KAKAO"), GOOGLE("GOOGLE"), NAVER("NAVER");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }
}