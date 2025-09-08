package com.eager.questioncloud.common.mail

interface EmailSender {
    fun send(email: Email)
}