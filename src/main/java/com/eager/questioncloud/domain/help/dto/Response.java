package com.eager.questioncloud.domain.help.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class RecoverForgottenEmailResponse {
        private String email;
    }
}
