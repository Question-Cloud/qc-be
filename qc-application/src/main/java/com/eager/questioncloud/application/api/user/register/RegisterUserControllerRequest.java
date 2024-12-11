package com.eager.questioncloud.application.api.user.register;

import com.eager.questioncloud.application.validator.EmailValidator;
import com.eager.questioncloud.application.validator.PasswordValidator;
import com.eager.questioncloud.application.validator.Validatable;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.util.StringUtils;

public class RegisterUserControllerRequest {
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

        public CreateUserRequest(String email, String password, String socialRegisterToken, AccountType accountType, String phone, String name) {
            this.email = email;
            this.password = password;
            this.socialRegisterToken = socialRegisterToken;
            this.accountType = accountType;
            this.phone = phone;
            this.name = name;
            validate();
        }

        @Override
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

        public CreateUser toCreateUser() {
            return new CreateUser(email, password, socialRegisterToken, accountType, phone, name);
        }
    }
}
