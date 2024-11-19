package com.eager.questioncloud.application.user.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class RegisterUserControllerResponse {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CreateUserResponse {
        private String resendToken;
    }
}
