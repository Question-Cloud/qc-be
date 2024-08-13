package com.eager.questioncloud.mail;

public interface EmailVerificationRepository {
    EmailVerification append(EmailVerification emailVerification);

    EmailVerification find(String token, EmailVerificationType emailVerificationType);

    EmailVerificationWithUser findForResend(String resendToken);

    EmailVerification findForException(Long uid);

    EmailVerification save(EmailVerification emailVerification);
}
