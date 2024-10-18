package com.eager.questioncloud.verification.repository;

import com.eager.questioncloud.verification.entity.EmailVerificationEntity;
import com.eager.questioncloud.verification.vo.EmailVerificationType;
import jakarta.persistence.Tuple;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, String> {
    Optional<EmailVerificationEntity> findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
        String token, EmailVerificationType emailVerificationType);

    @Query(value = "SELECT e as emailVerification, u as user from EmailVerificationEntity e left join UserEntity u on u.uid = e.uid where e.resendToken =:resendToken and e.isVerified = false")
    Optional<Tuple> findByResendTokenWithUser(String resendToken);

    Optional<EmailVerificationEntity> findByUidAndIsVerifiedFalse(Long uid);
}
