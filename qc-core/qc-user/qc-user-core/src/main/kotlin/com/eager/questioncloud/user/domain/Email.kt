package com.eager.questioncloud.user.domain

import com.eager.questioncloud.user.template.EmailVerificationTemplateCreator

class Email(
    val to: String,
    val subject: String,
    val content: String
) {
    companion object {
        fun of(emailVerification: EmailVerification): Email {
            val template = EmailVerificationTemplateCreator.getTemplate(
                emailVerification.emailVerificationType,
                emailVerification.token
            )

            return Email(
                emailVerification.email,
                template.title,
                template.content
            )
        }
    }
}
