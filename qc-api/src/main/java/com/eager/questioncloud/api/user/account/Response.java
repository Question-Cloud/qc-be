package com.eager.questioncloud.api.user.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class RecoverForgottenEmailResponse {
        private String email;
    }
}
