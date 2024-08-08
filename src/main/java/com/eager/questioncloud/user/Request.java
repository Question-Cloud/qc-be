package com.eager.questioncloud.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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

        private String socialUid;

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
            socialUidValidate();
        }

        public void loginIdValidate() {
            if (!Pattern.matches("^[a-zA-Z0-9]{6,20}$", loginId)) {
                throw new RuntimeException();
            }
        }

        public void passwordValidate() {
            if (!Pattern.matches("^(?!.*\\s).{8,}$", password)) {
                throw new RuntimeException();
            }
        }

        public void socialUidValidate() {
            if (!StringUtils.hasText(socialUid)) {
                throw new RuntimeException();
            }
        }
    }
}
