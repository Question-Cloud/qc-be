package com.eager.questioncloud.api.user.account;

import com.eager.questioncloud.validator.PasswordValidator;
import com.eager.questioncloud.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class Request {
    @Getter
    public static class ChangePasswordRequest implements Validatable {
        @NotBlank
        private String token;
        private String newPassword;

        public void validate() {
            PasswordValidator.validate(newPassword);
        }
    }
}
