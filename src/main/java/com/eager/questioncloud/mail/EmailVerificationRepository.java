package com.eager.questioncloud.mail;

public interface EmailVerificationRepository {
    EmailVerification append(EmailVerification emailVerification);

    EmailVerification find(String token, EmailVerificationType emailVerificationType);

    EmailVerification save(EmailVerification emailVerification);
}
