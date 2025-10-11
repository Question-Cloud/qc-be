package com.eager.questioncloud.review.command

data class ModifyReviewCommand(
    val reviewId: Long,
    val reviewerId: Long,
    val comment: String,
    val rate: Int
)
