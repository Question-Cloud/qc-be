package com.eager.questioncloud.review.infrastructure.repository

import com.eager.questioncloud.review.infrastructure.entity.QuestionReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QuestionReviewJpaRepository : JpaRepository<QuestionReviewEntity, Long> {
    fun findByQuestionIdAndReviewerIdAndIsDeletedFalse(
        questionId: Long,
        reviewerId: Long
    ): Optional<QuestionReviewEntity>

    fun findByIdAndReviewerIdAndIsDeletedFalse(id: Long, reviewerId: Long): Optional<QuestionReviewEntity>
}
