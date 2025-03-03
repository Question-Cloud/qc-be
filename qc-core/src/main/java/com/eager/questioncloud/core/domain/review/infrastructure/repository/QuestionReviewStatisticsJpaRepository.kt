package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewStatisticsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionReviewStatisticsJpaRepository : JpaRepository<QuestionReviewStatisticsEntity, Long> {
    fun findByQuestionIdIn(questionIds: List<Long>): List<QuestionReviewStatisticsEntity>
}
