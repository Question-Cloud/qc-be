package com.eager.questioncloud.mail;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.EmailVerificationWithUser;
import com.eager.questioncloud.user.User;
import com.eager.questioncloud.user.UserEntity;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {
    private final EmailVerificationJpaRepository emailVerificationJpaRepository;

    @Override
    public EmailVerification append(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(emailVerification.toEntity()).toDomain();
    }

    @Override
    public EmailVerification find(String token, EmailVerificationType emailVerificationType) {
        return emailVerificationJpaRepository.findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(token, emailVerificationType)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toDomain();
    }

    @Override
    public EmailVerificationWithUser findForResend(String resendToken) {
        Tuple result = emailVerificationJpaRepository.findByResendTokenWithUser(resendToken)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND));
        EmailVerification emailVerification = result.get("emailVerification", EmailVerificationEntity.class).toDomain();
        User user = result.get("user", UserEntity.class).toDomain();
        return new EmailVerificationWithUser(emailVerification, user);
    }

    @Override
    public EmailVerification findForException(Long uid) {
        return emailVerificationJpaRepository.findByUidAndIsVerifiedFalse(uid)
            .orElseThrow(() -> new CustomException(Error.INTERNAL_SERVER_ERROR))
            .toDomain();
    }

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(emailVerification.toEntity()).toDomain();
    }
}
