package com.eager.questioncloud.verification.vo;

import lombok.Getter;

@Getter
public enum EmailVerificationType {
    CreateUser("CreateUser"), ChangePassword("ChangePassword");

    private final String value;

    EmailVerificationType(String value) {
        this.value = value;
    }
}
