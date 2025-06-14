package com.eager.questioncloud.review.infrastructure.repository

import com.eager.questioncloud.review.domain.QuestionReviewEventLog

interface QuestionReviewEventLogRepository {
    fun findByEventId(eventId: String): QuestionReviewEventLog

    fun getUnPublishedEvent(): List<QuestionReviewEventLog>

    fun save(questionReviewEventLog: QuestionReviewEventLog)

    fun publish(eventId: String)

    fun publish(eventIds: List<String>)
}