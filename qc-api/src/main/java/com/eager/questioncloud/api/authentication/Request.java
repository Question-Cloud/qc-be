package com.eager.questioncloud.api.authentication;

import com.eager.questioncloud.validator.EmailValidator;
import com.eager.questioncloud.validator.PasswordValidator;
import com.eager.questioncloud.validator.Validatable;
import lombok.Getter;

public class Request {
    @Getter
    public static class LoginRequest implements Validatable {
        private String email;
        private String password;

        public void validate() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
        }
    }
}
