package com.eager.questioncloud.core.domain.review.infrastructure.entity

import com.eager.questioncloud.core.domain.review.model.QuestionReview
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question_review")
class QuestionReviewEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id private var id: Long?,
    @Column private var questionId: Long,
    @Column private var reviewerId: Long,
    @Column private var comment: String,
    @Column private var rate: Int,
    @Column private var createdAt: LocalDateTime,
    @Column private var isDeleted: Boolean
) {
    fun toModel(): QuestionReview {
        return QuestionReview(id, questionId, reviewerId, comment, rate, createdAt, isDeleted)
    }

    companion object {
        @JvmStatic
        fun from(questionReview: QuestionReview): QuestionReviewEntity {
            return QuestionReviewEntity(
                questionReview.id,
                questionReview.questionId,
                questionReview.reviewerId,
                questionReview.comment,
                questionReview.rate,
                questionReview.createdAt,
                questionReview.isDeleted
            )
        }
    }
}
