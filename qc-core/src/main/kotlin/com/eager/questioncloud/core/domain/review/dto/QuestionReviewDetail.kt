package com.eager.questioncloud.core.domain.review.dto

import java.time.LocalDateTime

class QuestionReviewDetail(
    val id: Long,
    val reviewerName: String,
    val reviewerStatistics: ReviewerStatistics,
    val rate: Int,
    val comment: String,
    val isWriter: Boolean,
    val createdAt: LocalDateTime,
)

class ReviewerStatistics(
    val reviewCount: Int,
    val rateAverage: Double,
)
