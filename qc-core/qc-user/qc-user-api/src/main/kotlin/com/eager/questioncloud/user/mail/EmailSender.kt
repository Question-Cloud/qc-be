package com.eager.questioncloud.user.mail

import com.eager.questioncloud.user.domain.Email
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class EmailSender(
    private val javaMailSender: JavaMailSender
) {
    @Async
    fun sendMail(email: Email) {
        val mimeMessage = javaMailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")

        mimeMessageHelper.setTo(email.to)
        mimeMessageHelper.setSubject(email.subject)
        mimeMessageHelper.setText(email.content, true)
        javaMailSender.send(mimeMessage)
    }
}
