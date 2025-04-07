package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentEventLog

interface QuestionPaymentEventLogRepository {
    fun save(questionPaymentEventLog: QuestionPaymentEventLog)

    fun getUnPublishedEvent(): List<QuestionPaymentEventLog>

    fun findByEventId(eventId: String): QuestionPaymentEventLog

    fun publish(eventId: String)
}