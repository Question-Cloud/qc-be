package com.eager.questioncloud.mail;

import com.eager.questioncloud.user.EmailVerificationWithUser;

public interface EmailVerificationRepository {
    EmailVerification append(EmailVerification emailVerification);

    EmailVerification find(String token, EmailVerificationType emailVerificationType);

    EmailVerificationWithUser findForResend(String resendToken);

    EmailVerification findForException(Long uid);

    EmailVerification save(EmailVerification emailVerification);
}
