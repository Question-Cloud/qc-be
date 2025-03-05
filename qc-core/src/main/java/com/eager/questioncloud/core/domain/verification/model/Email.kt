package com.eager.questioncloud.core.domain.verification.model

import com.eager.questioncloud.core.domain.verification.template.EmailVerificationTemplateCreator

class Email(
    val to: String,
    val subject: String?,
    val content: String?
) {
    companion object {
        @JvmStatic
        fun of(emailVerification: EmailVerification): Email {
            val template = EmailVerificationTemplateCreator.getTemplate(
                emailVerification.emailVerificationType,
                emailVerification.token
            )

            return Email(emailVerification.email, template.title, template.content)
        }
    }
}
