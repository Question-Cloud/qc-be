package com.eager.questioncloud.application.api.user.account;

import com.eager.questioncloud.application.validator.PasswordValidator;
import com.eager.questioncloud.application.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class UserAccountControllerRequest {
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
