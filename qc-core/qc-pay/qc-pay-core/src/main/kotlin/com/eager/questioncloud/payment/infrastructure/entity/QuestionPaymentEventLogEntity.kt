package com.eager.questioncloud.payment.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.payment.domain.QuestionPaymentEventLog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "question_payment_event_log")
class QuestionPaymentEventLogEntity(
    @Id var eventId: String,
    @Column val isPublish: Boolean,
    @Column(columnDefinition = "TEXT") val payload: String,
    @Column val createdAt: LocalDateTime,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    fun toModel(): QuestionPaymentEventLog {
        return QuestionPaymentEventLog(eventId, isPublish, payload, createdAt)
    }

    companion object {
        fun createNewEntity(questionPaymentEventLog: QuestionPaymentEventLog): QuestionPaymentEventLogEntity {
            return QuestionPaymentEventLogEntity(
                questionPaymentEventLog.eventId,
                questionPaymentEventLog.isPublish,
                questionPaymentEventLog.payload,
                questionPaymentEventLog.createdAt,
                true
            )
        }
    }

    override fun getId(): String {
        return eventId
    }
}