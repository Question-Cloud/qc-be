package com.eager.questioncloud.core.domain.review.dto

import java.time.LocalDateTime

class QuestionReviewDetail(
    val id: Long,
    val name: String,
    val isCreator: Boolean,
    val isWriter: Boolean,
    val reviewCount: Int,
    val rateAverage: Double,
    val rate: Int,
    val comment: String,
    val createdAt: LocalDateTime,
) {

}
