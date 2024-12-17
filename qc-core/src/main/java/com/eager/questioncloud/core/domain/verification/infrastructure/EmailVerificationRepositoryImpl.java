package com.eager.questioncloud.core.domain.verification.infrastructure;

import static com.eager.questioncloud.core.domain.verification.infrastructure.QEmailVerificationEntity.emailVerificationEntity;

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
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
            throw new CoreException(Error.NOT_FOUND);
        }

        return result.toModel();
    }

    @Override
    public EmailVerification getForResend(String resendToken) {
        EmailVerificationEntity result = jpaQueryFactory.select(emailVerificationEntity)
            .from(emailVerificationEntity)
            .where(emailVerificationEntity.isVerified.isFalse(), emailVerificationEntity.resendToken.eq(resendToken))
            .fetchFirst();

        if (result == null) {
            throw new CoreException(Error.NOT_FOUND);
        }

        return result.toModel();
    }

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(EmailVerificationEntity.from(emailVerification)).toModel();
    }
}
