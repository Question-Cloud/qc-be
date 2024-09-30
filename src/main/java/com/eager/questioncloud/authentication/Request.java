package com.eager.questioncloud.authentication;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.valid.EmailValidator;
import java.util.regex.Pattern;
import lombok.Getter;

public class Request {
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;

        public void validate() {
            EmailValidator.validate(email);
            passwordValidate();
        }

        public void passwordValidate() {
            if (!Pattern.matches("^(?!.*\\s).{8,}$", password)) {
                throw new CustomException(Error.BAD_REQUEST);
            }
        }
    }
}
