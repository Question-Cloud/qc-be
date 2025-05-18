package com.eager.questioncloud.core.domain.review.model

import java.time.LocalDateTime

class QuestionReviewEventLog(
    var eventId: String,
    val isPublish: Boolean,
    val payload: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(eventId: String, payload: String): QuestionReviewEventLog {
            return QuestionReviewEventLog(eventId, false, payload, LocalDateTime.now())
        }
    }
}