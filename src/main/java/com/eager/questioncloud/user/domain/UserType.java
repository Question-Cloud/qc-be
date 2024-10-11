package com.eager.questioncloud.user.domain;

import lombok.Getter;

@Getter
public enum UserType {
    NormalUser("NormalUser"), CreatorUser("CreatorUser");
    private String value;

    UserType(String value) {
        this.value = value;
    }
}
