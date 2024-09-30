package com.eager.questioncloud.user;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.valid.EmailValidator;
import com.eager.questioncloud.valid.PasswordValidator;
import com.eager.questioncloud.valid.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.util.StringUtils;

public class Request {
    @Getter
    public static class CreateUserRequest implements Validatable {
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
                PasswordValidator.validate(password);
                return;
            }
            socialRegisterTokenValidate();
        }

        public void socialRegisterTokenValidate() {
            if (!StringUtils.hasText(socialRegisterToken)) {
                throw new CustomException(Error.BAD_REQUEST);
            }
        }
    }

    @Getter
    public static class ChangePasswordRequest implements Validatable {
        @NotBlank
        private String token;
        private String newPassword;

        public void validate() {
            PasswordValidator.validate(newPassword);
        }
    }

    @Getter
    public static class UpdateMyInformationRequest {
        private String profileImage;
        private String name;
    }
}
