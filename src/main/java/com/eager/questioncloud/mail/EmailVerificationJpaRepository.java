package com.eager.questioncloud.mail;

import jakarta.persistence.Tuple;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, String> {
    Optional<EmailVerificationEntity> findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        String token, EmailVerificationType emailVerificationType);

    @Query(value = "SELECT e as emailVerification, u as user from EmailVerificationEntity e left join UserEntity u on u.uid = e.uid where e.resendToken =:resendToken and e.isVerified = false")
    Optional<Tuple> findByResendTokenWithUser(String resendToken);
}
