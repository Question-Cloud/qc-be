package com.eager.questioncloud.help;

import lombok.Getter;

public class Request {
    @Getter
    public static class RecoverForgottenEmailRequest {
        private String phone;
    }
}
