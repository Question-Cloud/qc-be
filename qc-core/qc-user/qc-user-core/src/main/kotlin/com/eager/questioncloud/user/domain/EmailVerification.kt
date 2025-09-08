package com.eager.questioncloud.user.domain

import com.eager.questioncloud.common.mail.Email
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.template.EmailVerificationTemplateCreator
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
    
    fun toEmail(): Email {
        val template = EmailVerificationTemplateCreator.getTemplate(emailVerificationType, token)
        return Email(email, template.title, template.content)
    }
}
