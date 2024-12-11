package com.eager.questioncloud.application.api.user.register.dto;

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
