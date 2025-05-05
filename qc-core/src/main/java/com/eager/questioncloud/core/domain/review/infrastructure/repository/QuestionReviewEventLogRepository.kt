package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog

interface QuestionReviewEventLogRepository {
    fun findByEventId(eventId: String): QuestionReviewEventLog

    fun getUnPublishedEvent(): List<QuestionReviewEventLog>

    fun save(questionReviewEventLog: QuestionReviewEventLog)

    fun publish(eventId: String)

    fun publish(eventIds: List<String>)
}