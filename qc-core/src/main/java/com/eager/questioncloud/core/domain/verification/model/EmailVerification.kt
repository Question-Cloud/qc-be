package com.eager.questioncloud.core.domain.verification.model

import com.eager.questioncloud.core.domain.verification.enums.EmailVerificationType
import java.util.*

class EmailVerification(
    val token: String = UUID.randomUUID().toString(),
    val resendToken: String = UUID.randomUUID().toString(),
    val uid: Long,
    val email: String,
    val emailVerificationType: EmailVerificationType,
    var isVerified: Boolean = false,
) {
    fun verify() {
        this.isVerified = true
    }

    companion object {
        @JvmStatic
        fun create(
            uid: Long,
            email: String,
            emailVerificationType: EmailVerificationType
        ): EmailVerification {
            return EmailVerification(
                uid = uid,
                email = email,
                emailVerificationType = emailVerificationType
            )
        }
    }
}
