package com.eager.questioncloud.api.user.register;

import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.validator.EmailValidator;
import com.eager.questioncloud.validator.PasswordValidator;
import com.eager.questioncloud.validator.Validatable;
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
}
