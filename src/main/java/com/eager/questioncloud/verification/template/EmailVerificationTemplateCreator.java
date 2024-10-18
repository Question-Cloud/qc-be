package com.eager.questioncloud.verification.template;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.verification.vo.EmailVerificationType;

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
