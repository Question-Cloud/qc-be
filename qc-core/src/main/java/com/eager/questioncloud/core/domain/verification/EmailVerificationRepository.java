package com.eager.questioncloud.core.domain.verification;


public interface EmailVerificationRepository {
    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getCreateUserVerification(Long userId);

    EmailVerification getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
