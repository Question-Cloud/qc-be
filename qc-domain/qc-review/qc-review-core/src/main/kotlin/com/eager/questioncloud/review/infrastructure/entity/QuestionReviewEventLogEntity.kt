package com.eager.questioncloud.review.infrastructure.entity

import com.eager.questioncloud.entity.BaseCustomIdEntity
import com.eager.questioncloud.review.domain.QuestionReviewEventLog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "question_review_event_log")
class QuestionReviewEventLogEntity(
    @Id var eventId: String,
    @Column val isPublish: Boolean,
    @Column(columnDefinition = "TEXT") val payload: String,
    @Column val createdAt: LocalDateTime,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    fun toModel(): QuestionReviewEventLog {
        return QuestionReviewEventLog(eventId, isPublish, payload, createdAt)
    }

    companion object {
        fun createNewEntity(reviewEventLog: QuestionReviewEventLog): QuestionReviewEventLogEntity {
            return QuestionReviewEventLogEntity(
                reviewEventLog.eventId,
                reviewEventLog.isPublish,
                reviewEventLog.payload,
                reviewEventLog.createdAt,
                true
            )
        }
    }

    override fun getId(): String {
        return eventId
    }
}