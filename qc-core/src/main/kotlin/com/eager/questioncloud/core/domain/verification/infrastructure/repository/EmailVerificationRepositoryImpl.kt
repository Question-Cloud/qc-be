package com.eager.questioncloud.core.domain.verification.infrastructure.repository

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.infrastructure.entity.EmailVerificationEntity.Companion.from
import com.eager.questioncloud.core.domain.verification.infrastructure.entity.QEmailVerificationEntity.emailVerificationEntity
import com.eager.questioncloud.core.domain.verification.model.EmailVerification
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class EmailVerificationRepositoryImpl(
    private val emailVerificationJpaRepository: EmailVerificationJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : EmailVerificationRepository {
    override fun get(token: String, emailVerificationType: EmailVerificationType): EmailVerification {
        return emailVerificationJpaRepository.findByTokenAndEmailVerificationTypeAndIsVerifiedFalse(
            token,
            emailVerificationType
        )
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun getCreateUserVerification(userId: Long): EmailVerification {
        return jpaQueryFactory.select(emailVerificationEntity)
            .from(emailVerificationEntity)
            .where(
                emailVerificationEntity.uid.eq(userId),
                emailVerificationEntity.isVerified.isFalse(),
                emailVerificationEntity.emailVerificationType.eq(EmailVerificationType.CreateUser)
            )
            .fetchFirst()?.toModel() ?: throw CoreException(Error.NOT_FOUND)
    }

    override fun getForResend(resendToken: String): EmailVerification {
        return jpaQueryFactory.select(emailVerificationEntity)
            .from(emailVerificationEntity)
            .where(
                emailVerificationEntity.isVerified.isFalse(),
                emailVerificationEntity.resendToken.eq(resendToken)
            )
            .fetchFirst()?.toModel() ?: throw CoreException(Error.NOT_FOUND)
    }

    override fun save(emailVerification: EmailVerification): EmailVerification {
        return emailVerificationJpaRepository.save(from(emailVerification)).toModel()
    }
}
