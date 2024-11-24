package com.eager.questioncloud.core.domain.user;

import lombok.Getter;

@Getter
public enum UserType {
    NormalUser("NormalUser"), CreatorUser("CreatorUser");
    private String value;

    UserType(String value) {
        this.value = value;
    }
}
