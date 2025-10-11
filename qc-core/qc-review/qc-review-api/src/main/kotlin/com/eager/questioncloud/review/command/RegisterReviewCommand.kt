package com.eager.questioncloud.review.command

data class RegisterReviewCommand(
    val reviewerId: Long,
    val questionId: Long,
    val comment: String,
    val rate: Int,
)
