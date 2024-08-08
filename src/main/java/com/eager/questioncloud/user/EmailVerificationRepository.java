package com.eager.questioncloud.user;

public interface EmailVerificationRepository {
    EmailVerification append(EmailVerification emailVerification);

    EmailVerification find(String token, EmailVerificationType emailVerificationType);

    EmailVerification save(EmailVerification emailVerification);
}
