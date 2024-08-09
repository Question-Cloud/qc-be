package com.eager.questioncloud.user;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.util.StringUtils;

public class Request {
    @Getter
    public static class CreateUserRequest {
        private String loginId;

        private String password;

        private String socialRegisterToken;

        private AccountType accountType;

        @NotBlank
        private String phone;

        @NotBlank
        @Size(min = 2)
        private String name;

        @Email
        private String email;

        public void validate() {
            if (accountType.equals(AccountType.ID)) {
                loginIdValidate();
                passwordValidate();
                return;
            }
            socialRegisterTokenValidate();
        }

        public void loginIdValidate() {
            if (!Pattern.matches("^[a-zA-Z0-9]{6,20}$", loginId)) {
                throw new CustomException(Error.BAD_REQUEST);
            }
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

    @Getter
    public static class LoginRequest {
        private String loginId;
        private String password;
    }
}
