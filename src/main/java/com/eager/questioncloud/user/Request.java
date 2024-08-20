package com.eager.questioncloud.user;

import com.eager.questioncloud.common.EmailValidator;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.util.StringUtils;

public class Request {
    @Getter
    public static class CreateUserRequest {
        private String email;

        private String password;

        private String socialRegisterToken;

        private AccountType accountType;

        @NotBlank
        private String phone;

        @NotBlank
        @Size(min = 2)
        private String name;

        public void validate() {
            if (accountType.equals(AccountType.EMAIL)) {
                EmailValidator.validate(email);
                passwordValidate();
                return;
            }
            socialRegisterTokenValidate();
        }

        public void passwordValidate() {
            if (!Pattern.matches("^(?!.*\\s).{8,}$", password)) {
                throw new CustomException(Error.BAD_REQUEST);
            }
        }

        public void socialRegisterTokenValidate() {
            if (!StringUtils.hasText(socialRegisterToken)) {
                throw new CustomException(Error.BAD_REQUEST);
            }
        }
    }
}
