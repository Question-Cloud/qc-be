package com.eager.questioncloud.authentication.dto;

import com.eager.questioncloud.valid.EmailValidator;
import com.eager.questioncloud.valid.PasswordValidator;
import com.eager.questioncloud.valid.Validatable;
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
