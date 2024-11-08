package com.eager.questioncloud.core.domain.verification.repository;


import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;

public interface EmailVerificationRepository {
    EmailVerification get(String token, EmailVerificationType emailVerificationType);

    EmailVerification getCreateUserVerification(Long userId);

    EmailVerification getForResend(String resendToken);

    EmailVerification save(EmailVerification emailVerification);
}
