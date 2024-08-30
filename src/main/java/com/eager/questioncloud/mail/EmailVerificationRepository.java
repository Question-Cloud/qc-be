package com.eager.questioncloud.mail;

public interface EmailVerificationRepository {
    EmailVerification append(EmailVerification emailVerification);

    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getForNotVerifiedUser(Long userId);

    EmailVerificationWithUser getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
