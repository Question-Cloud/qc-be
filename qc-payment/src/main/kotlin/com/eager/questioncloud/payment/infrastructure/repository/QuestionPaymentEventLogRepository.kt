package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.domain.QuestionPaymentEventLog

interface QuestionPaymentEventLogRepository {
    fun save(questionPaymentEventLog: QuestionPaymentEventLog)

    fun getUnPublishedEvent(): List<QuestionPaymentEventLog>

    fun findByEventId(eventId: String): QuestionPaymentEventLog

    fun publish(eventId: String)

    fun publish(eventIds: List<String>)
}