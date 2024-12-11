package com.eager.questioncloud.application.api.user.account.dto;

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

        public ChangePasswordRequest(String token, String newPassword) {
            this.token = token;
            this.newPassword = newPassword;
            validate();
        }

        public void validate() {
            PasswordValidator.validate(newPassword);
        }
    }
}
