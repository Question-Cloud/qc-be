package com.eager.questioncloud.domain.verification;


public interface EmailVerificationRepository {
    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getCreateUserVerification(Long userId);

    EmailVerification getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
