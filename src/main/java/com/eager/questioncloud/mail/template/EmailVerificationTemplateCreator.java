package com.eager.questioncloud.mail.template;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.mail.template.ChangePasswordEmailVerificationTemplate;
import com.eager.questioncloud.mail.template.CreateUserEmailVerificationTemplate;
import com.eager.questioncloud.mail.template.EmailVerificationTemplate;

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
