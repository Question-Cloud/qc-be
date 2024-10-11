package com.eager.questioncloud.user.vo;

import lombok.Getter;

@Getter
public enum UserStatus {
    PendingEmailVerification("PendingEmailVerification"), Active("Active"), Deleted("Deleted"), Ban("Ban");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }
}
