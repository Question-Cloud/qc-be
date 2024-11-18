package com.eager.questioncloud.domain.verification;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, String> {
    Optional<EmailVerificationEntity> findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        String token, EmailVerificationType emailVerificationType);

    Optional<EmailVerificationEntity> findByResendToken(String resendToken);
}
