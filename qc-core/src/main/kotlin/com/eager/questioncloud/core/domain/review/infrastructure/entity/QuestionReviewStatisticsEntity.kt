package com.eager.questioncloud.core.domain.review.infrastructure.entity

import com.eager.questioncloud.core.common.BaseCustomIdEntity
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "question_review_statistics")
class QuestionReviewStatisticsEntity private constructor(
    @Id private var questionId: Long,
    @Column private var reviewCount: Int,
    @Column private var totalRate: Int,
    @Column private var averageRate: Double,
    isNewEntity: Boolean
) : BaseCustomIdEntity<Long>(isNewEntity) {
    fun toModel(): QuestionReviewStatistics {
        return QuestionReviewStatistics(questionId, reviewCount, totalRate, averageRate)
    }

    companion object {
        fun createNewEntity(questionReviewStatistics: QuestionReviewStatistics): QuestionReviewStatisticsEntity {
            return QuestionReviewStatisticsEntity(
                questionReviewStatistics.questionId,
                questionReviewStatistics.reviewCount,
                questionReviewStatistics.totalRate,
                questionReviewStatistics.averageRate,
                true
            )
        }

        fun fromExisting(questionReviewStatistics: QuestionReviewStatistics): QuestionReviewStatisticsEntity {
            return QuestionReviewStatisticsEntity(
                questionReviewStatistics.questionId,
                questionReviewStatistics.reviewCount,
                questionReviewStatistics.totalRate,
                questionReviewStatistics.averageRate,
                false
            )
        }
    }

    override fun getId(): Long {
        return questionId
    }
}
