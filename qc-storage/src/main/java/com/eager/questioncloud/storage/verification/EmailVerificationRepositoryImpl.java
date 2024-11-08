package com.eager.questioncloud.storage.verification;

import static com.eager.questioncloud.storage.verification.QEmailVerificationEntity.emailVerificationEntity;

import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.verification.dto.EmailVerificationWithUser;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.repository.EmailVerificationRepository;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.storage.user.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {
    private final EmailVerificationJpaRepository emailVerificationJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public EmailVerification get(String token, EmailVerificationType emailVerificationType) {
        return emailVerificationJpaRepository.findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(token, emailVerificationType)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public EmailVerification getCreateUserVerification(Long userId) {
        EmailVerificationEntity result = jpaQueryFactory.select(emailVerificationEntity)
            .from(emailVerificationEntity)
            .where(
                emailVerificationEntity.uid.eq(userId),
                emailVerificationEntity.isVerified.isFalse(),
                emailVerificationEntity.emailVerificationType.eq(EmailVerificationType.CreateUser))
            .fetchFirst();

        if (result == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return result.toModel();
    }

    @Override
    public EmailVerificationWithUser findByResendToken(String resendToken) {
        Tuple result = emailVerificationJpaRepository.findByResendTokenWithUser(resendToken)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND));
        EmailVerification emailVerification = result.get("emailVerification", EmailVerificationEntity.class).toModel();
        User user = result.get("user", UserEntity.class).toModel();
        return new EmailVerificationWithUser(emailVerification, user);
    }

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(EmailVerificationEntity.from(emailVerification)).toModel();
    }
}
