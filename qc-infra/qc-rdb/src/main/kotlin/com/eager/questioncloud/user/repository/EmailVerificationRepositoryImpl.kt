package com.eager.questioncloud.user.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.entity.EmailVerificationEntity
import com.eager.questioncloud.user.entity.QEmailVerificationEntity.emailVerificationEntity
import com.eager.questioncloud.user.enums.EmailVerificationType
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
        return emailVerificationJpaRepository.save(EmailVerificationEntity.createNewEntity(emailVerification)).toModel()
    }
    
    override fun update(emailVerification: EmailVerification) {
        emailVerificationJpaRepository.save(EmailVerificationEntity.fromExisting(emailVerification))
    }
}
