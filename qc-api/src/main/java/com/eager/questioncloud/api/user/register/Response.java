package com.eager.questioncloud.api.user.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Response {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CreateUserResponse {
        private String resendToken;
    }
}
