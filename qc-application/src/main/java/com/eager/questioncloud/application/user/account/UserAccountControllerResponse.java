package com.eager.questioncloud.application.user.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserAccountControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class RecoverForgottenEmailResponse {
        private String email;
    }
}
