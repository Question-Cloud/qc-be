package com.eager.questioncloud.payment.domain

import java.time.LocalDateTime

class QuestionPaymentEventLog(
    val eventId: String,
    val isPublish: Boolean,
    val payload: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(eventId: String, payload: String): QuestionPaymentEventLog {
            return QuestionPaymentEventLog(eventId, false, payload, LocalDateTime.now())
        }
    }
}