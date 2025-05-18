package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewEventLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionReviewEventLogJpaRepository : JpaRepository<QuestionReviewEventLogEntity, String> {
}