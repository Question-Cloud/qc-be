package com.eager.questioncloud.core.domain.verification.infrastructure.entity

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import com.eager.questioncloud.core.domain.verification.model.EmailVerification
import jakarta.persistence.*

@Entity
@Table(name = "email_verification")
class EmailVerificationEntity private constructor(
    @Id var token: String,
    @Column var resendToken: String,
    @Column var uid: Long,
    @Column var email: String,
    @Enumerated(EnumType.STRING) @Column var emailVerificationType: EmailVerificationType,
    @Column var isVerified: Boolean
) {
    fun toModel(): EmailVerification {
        return EmailVerification(token, resendToken, uid, email, emailVerificationType, isVerified)
    }

    companion object {
        @JvmStatic
        fun from(emailVerification: EmailVerification): EmailVerificationEntity {
            return EmailVerificationEntity(
                emailVerification.token,
                emailVerification.resendToken,
                emailVerification.uid,
                emailVerification.email,
                emailVerification.emailVerificationType,
                emailVerification.isVerified
            )
        }
    }
}
