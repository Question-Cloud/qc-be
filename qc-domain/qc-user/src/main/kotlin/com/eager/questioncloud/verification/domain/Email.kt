package com.eager.questioncloud.verification.domain

class Email(
    val to: String,
    val subject: String,
    val content: String
) {
    companion object {
        fun of(emailVerification: EmailVerification): Email {
            val template = com.eager.questioncloud.verification.template.EmailVerificationTemplateCreator.getTemplate(
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
