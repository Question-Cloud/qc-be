package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewStatisticsEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface QuestionReviewStatisticsJpaRepository : JpaRepository<QuestionReviewStatisticsEntity, Long> {
    fun findByQuestionIdIn(questionIds: List<Long>): List<QuestionReviewStatisticsEntity>
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByQuestionId(questionId: Long): QuestionReviewStatisticsEntity?
}
