package com.eager.questioncloud.application.api.authentication.dto;

import com.eager.questioncloud.application.validator.EmailValidator;
import com.eager.questioncloud.application.validator.PasswordValidator;
import com.eager.questioncloud.application.validator.Validatable;
import lombok.Getter;

public class AuthenticationRequest {
    @Getter
    public static class LoginRequest implements Validatable {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
            validate();
        }

        @Override
        public void validate() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
        }
    }
}
