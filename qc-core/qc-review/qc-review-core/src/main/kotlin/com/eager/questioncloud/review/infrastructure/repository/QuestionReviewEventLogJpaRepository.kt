package com.eager.questioncloud.review.infrastructure.repository

import com.eager.questioncloud.review.infrastructure.entity.QuestionReviewEventLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionReviewEventLogJpaRepository : JpaRepository<QuestionReviewEventLogEntity, String> {
}