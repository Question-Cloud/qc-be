package com.eager.questioncloud.mail

import com.eager.questioncloud.common.mail.Email
import com.eager.questioncloud.common.mail.EmailSender
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class MailSenderImpl(
    private val javaMailSender: JavaMailSender,
) : EmailSender {
    @Async
    override fun send(email: Email) {
        val mimeMessage = javaMailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")
        
        mimeMessageHelper.setTo(email.to)
        mimeMessageHelper.setSubject(email.subject)
        mimeMessageHelper.setText(email.content, true)
        javaMailSender.send(mimeMessage)
    }
}