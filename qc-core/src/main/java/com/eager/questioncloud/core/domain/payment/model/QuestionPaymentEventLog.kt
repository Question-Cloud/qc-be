package com.eager.questioncloud.core.domain.payment.model

import java.time.LocalDateTime

class QuestionPaymentEventLog(
    val eventId: String,
    val isPublish: Boolean,
    val payload: String,
    val createdAt: LocalDateTime
) {
}