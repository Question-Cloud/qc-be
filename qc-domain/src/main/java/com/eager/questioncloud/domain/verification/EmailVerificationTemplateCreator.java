package com.eager.questioncloud.domain.verification;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;

public class EmailVerificationTemplateCreator {
    public static EmailVerificationTemplate getTemplate(EmailVerificationType emailVerificationType, String token) {
        switch (emailVerificationType) {
            case CreateUser -> {
                return new CreateUserEmailVerificationTemplate(token);
            }
            case ChangePassword -> {
                return new ChangePasswordEmailVerificationTemplate(token);
            }
            default -> {
                throw new CustomException(Error.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
