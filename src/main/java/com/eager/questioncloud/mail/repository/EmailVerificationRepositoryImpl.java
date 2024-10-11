package com.eager.questioncloud.mail.repository;

import static com.eager.questioncloud.mail.entity.QEmailVerificationEntity.emailVerificationEntity;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.mail.dto.EmailVerificationWithUser;
import com.eager.questioncloud.mail.entity.EmailVerificationEntity;
import com.eager.questioncloud.user.domain.User;
import com.eager.questioncloud.user.entity.UserEntity;
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
            .toDomain();
    }

    @Override
    public EmailVerification getForNotVerifiedUser(Long userId) {
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

        return result.toDomain();
    }

    @Override
    public EmailVerificationWithUser getForResend(String resendToken) {
        Tuple result = emailVerificationJpaRepository.findByResendTokenWithUser(resendToken)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND));
        EmailVerification emailVerification = result.get("emailVerification", EmailVerificationEntity.class).toDomain();
        User user = result.get("user", UserEntity.class).toDomain();
        return new EmailVerificationWithUser(emailVerification, user);
    }

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(emailVerification.toEntity()).toDomain();
    }
}
