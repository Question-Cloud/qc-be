package com.eager.questioncloud.core.domain.verification.template;

import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;

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
