package com.eager.questioncloud.user.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.enums.EmailVerificationType
import jakarta.persistence.*

@Entity
@Table(name = "email_verification")
class EmailVerificationEntity private constructor(
    @Id var token: String,
    @Column var resendToken: String,
    @Column var uid: Long,
    @Column var email: String,
    @Enumerated(EnumType.STRING) @Column var emailVerificationType: EmailVerificationType,
    @Column var isVerified: Boolean,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    fun toModel(): EmailVerification {
        return EmailVerification(token, resendToken, uid, email, emailVerificationType, isVerified)
    }

    companion object {
        fun fromExisting(emailVerification: EmailVerification): EmailVerificationEntity {
            return EmailVerificationEntity(
                emailVerification.token,
                emailVerification.resendToken,
                emailVerification.uid,
                emailVerification.email,
                emailVerification.emailVerificationType,
                emailVerification.isVerified,
                false
            )
        }

        fun createNewEntity(emailVerification: EmailVerification): EmailVerificationEntity {
            return EmailVerificationEntity(
                emailVerification.token,
                emailVerification.resendToken,
                emailVerification.uid,
                emailVerification.email,
                emailVerification.emailVerificationType,
                emailVerification.isVerified,
                true
            )
        }
    }

    override fun getId(): String {
        return token
    }
}
