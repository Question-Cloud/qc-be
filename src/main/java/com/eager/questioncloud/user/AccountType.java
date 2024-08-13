package com.eager.questioncloud.user;

import lombok.Getter;

@Getter
public enum AccountType {
    ID("ID"), KAKAO("KAKAO"), GOOGLE("GOOGLE");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }
}
